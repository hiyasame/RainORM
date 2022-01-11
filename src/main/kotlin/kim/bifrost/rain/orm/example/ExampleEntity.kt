package kim.bifrost.rain.orm.example

import kim.bifrost.rain.orm.api.Column
import kim.bifrost.rain.orm.api.PrimaryKey
import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL

/**
 * kim.bifrost.rain.orm.example.ExampleEntity
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 14:51
 **/
data class ExampleEntity(
    // PrimaryKey 不用写option
    @PrimaryKey(autoGenerate = true)
    @Column(name = "id", type = ColumnTypeSQL.INT)
    val id: Int = 0,
    @Column(name = "type", type = ColumnTypeSQL.TEXT, options = [ColumnOptionSQL.NOTNULL])
    val type: String,
    @Column(name = "user", type = ColumnTypeSQL.TEXT, options = [ColumnOptionSQL.NOTNULL])
    val user: String,
    @Column(name = "data", type = ColumnTypeSQL.TEXT, def = "null")
    val data: String?
)