package kim.bifrost.rain.orm.api

import taboolib.module.database.Host
import taboolib.module.database.SQL
import taboolib.module.database.Table
import javax.sql.DataSource

/**
 * kim.bifrost.rain.orm.api.IDao
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 2:14
 **/
interface IDao<T> {
    /**
     * Data source
     */
    val dataSource: DataSource

    /**
     * SQL Table
     */
    val table: Table<Host<SQL>, SQL>

    /**
     * Table name
     */
    val tableName: String
}