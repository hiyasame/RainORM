package kim.bifrost.rain.orm.api

import kim.bifrost.rain.orm.internal.DynamicProxyHandler
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.database.HostSQL
import taboolib.module.database.getHost
import kotlin.properties.Delegates

/**
 * kim.bifrost.rain.orm.api.ORMBuilder
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 2:38
 **/
class ORMBuilder {

    private lateinit var host: String
    private var port by Delegates.notNull<Int>()
    private lateinit var user: String
    private lateinit var password: String
    private lateinit var database: String

    private lateinit var hostSQL: HostSQL

    fun buildFromConf(section: ConfigurationSection, name: String): ORMBuilder {
        hostSQL = section.getHost(name)
        return this
    }

    fun buildHost(): ORMBuilder {
        hostSQL = HostSQL(host, port.toString(), user, password, database)
        return this
    }

    fun host(host: String): ORMBuilder {
        this.host = host
        return this
    }

    fun port(port: Int): ORMBuilder {
        this.port = port
        return this
    }

    fun user(user: String): ORMBuilder {
        this.user = user
        return this
    }

    fun password(password: String): ORMBuilder {
        this.password = password
        return this
    }

    fun database(database: String): ORMBuilder {
        this.database = database
        return this
    }

    fun <T : IDao<*>> build(table: String, daoClazz: Class<T>): T {
        return DynamicProxyHandler(daoClazz, table, hostSQL).getInstance()
    }

    companion object {
        fun newBuilder(): ORMBuilder {
            return ORMBuilder()
        }
    }
}