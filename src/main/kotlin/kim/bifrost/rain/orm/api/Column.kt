package kim.bifrost.rain.orm.api

import taboolib.module.database.ColumnOptionSQL
import taboolib.module.database.ColumnTypeSQL

/**
 * kim.bifrost.rain.orm.api.Column
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 2:04
 **/
annotation class Column(
    val name: String,
    val type: ColumnTypeSQL,
    val options: Array<ColumnOptionSQL>,
    val def: String = ""
)
