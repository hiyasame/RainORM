package kim.bifrost.rain.orm.internal

import kim.bifrost.rain.orm.adaptResultSet
import kim.bifrost.rain.orm.api.*
import taboolib.common.reflect.ReflexClass
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.HostSQL
import taboolib.module.database.Table
import java.lang.reflect.*

/**
 * kim.bifrost.rain.orm.impl.DynamicProxyHandler
 * RainORM
 * 动态代理实现ORM功能
 *
 * @author 寒雨
 * @since 2022/1/11 15:10
 **/
class DynamicProxyHandler<T: IDao<*>>(
    private val clazz: Class<T>,
    private val tableName: String,
    private val host: HostSQL
    ) : InvocationHandler {

    private val entityClass by lazy { getEntityClazz() }

    private val dataSource by lazy { host.createDataSource() }

    private val table by lazy {
        Table(tableName, host) {
            val entityClazz = entityClass
            ReflexClass.find(entityClazz)
                .savingFields
                .forEach {
                    val column = it.getAnnotation(Column::class.java) ?: return@forEach
                    val primaryKey = it.getAnnotation(PrimaryKey::class.java)
                    add {
                        name(column.name)
                        type(column.type, column.parameter1, column.parameter2) {
                            options(*column.options)
                            primaryKey?.let { primaryKey ->
                                val options = arrayListOf(
                                    ColumnOptionSQL.PRIMARY_KEY,
                                    ColumnOptionSQL.NOTNULL,
                                    ColumnOptionSQL.UNIQUE_KEY
                                )
                                if (primaryKey.autoGenerate && column.type == ColumnTypeSQL.INT) {
                                    options.add(ColumnOptionSQL.AUTO_INCREMENT)
                                }
                                options(*options.toTypedArray())
                            }
                        }
                    }
                }
        }.apply { workspace(dataSource) { createTable(true) } }
    }

    @Suppress("UNCHECKED_CAST")
    fun getInstance(): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz), this) as T
    }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        when (method.name) {
            "getDataSource" -> return dataSource
            "getTable" -> return table
        }
        for (annotation in method.annotations) {
            when (annotation) {
                is DSL -> {
                    // TabooLib DSL
                    // Kt接口的默认方法为了兼容jdk8以下版本
                    // 实际上是在接口内生成了一个DefaultImpls的静态内部类来实现的
                    return ReflexClass.find(clazz.declaredClasses
                        .first { it.simpleName.endsWith("DefaultImpls") })
                        .savingMethods
                        .first { it.name == method.name }
                        .invoke(null, *arrayListOf(proxy).apply { args?.let { addAll(it) } }.toArray())
                }
                is Insert -> {
                    val insertObj = args!!.firstOrNull { entityClass.isInstance(it) } ?: error("insert function without a entity parameter")
                    table.workspace(dataSource) {
                        val insertMap = linkedMapOf<String, Any>().apply {
                            val fields = ReflexClass.find(entityClass).savingFields
                            fields.forEach {
                                val column = it.getAnnotation(Column::class.java) ?: return@forEach
                                println(it.get(insertObj))
                                put(column.name, it.get(insertObj))
                            }
                        }
                        insert(*insertMap.keys.toTypedArray()) { value(*insertMap.values.toTypedArray()) }
                    }
                }
                is Query -> {
                    val sql = annotation.sql.replace("{table}", tableName)
                    if (method.returnType == Void::class.java) {
                        dataSource.connection.createStatement().executeUpdate(sql)
                        return null
                    }
                    return dataSource.connection.createStatement().executeQuery(sql).adaptResultSet()
                }
            }
        }
        return null
    }

    private fun getEntityClazz() = (clazz.genericInterfaces
        .first { type -> type.typeName.replace(Regex("<\\S+>"), "").endsWith("IDao") } as ParameterizedType)
        .actualTypeArguments[0] as Class<*>
}