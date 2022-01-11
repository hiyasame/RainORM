package kim.bifrost.rain.orm

import kim.bifrost.rain.orm.api.Column
import taboolib.common.reflect.Reflex.Companion.setProperty
import taboolib.common.reflect.Reflex.Companion.unsafeInstance
import taboolib.common.reflect.ReflexClass
import java.sql.ResultSet

/**
 * kim.bifrost.rain.orm.Utils
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 21:42
 **/
/**
 * 直接转化ResultSet为Entity对象
 *
 * @param set
 * @return
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T> ResultSet.adaptResultSet(): T {
    val clazz = T::class.java
    val unsafe = clazz.unsafeInstance()
    // 使用Reflex缓存数据类，避免重复获取字段
    val fields = ReflexClass.find(clazz).savingFields
    fields.forEach {
        val column = it.getAnnotation(Column::class.java) ?: return@forEach
        unsafe.setProperty(it.name, getObject(column.name))
    }
    return unsafe as T
}