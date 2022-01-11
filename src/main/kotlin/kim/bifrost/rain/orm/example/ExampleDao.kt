package kim.bifrost.rain.orm.example

import kim.bifrost.rain.orm.adaptResultSet
import kim.bifrost.rain.orm.api.DSL
import kim.bifrost.rain.orm.api.IDao
import kim.bifrost.rain.orm.api.Insert
import kim.bifrost.rain.orm.api.Query

/**
 * kim.bifrost.rain.orm.example.ExampleDao
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 14:50
 **/
interface ExampleDao: IDao<ExampleEntity> {
    @Query("SELECT * FROM {table}")
    fun queryAll(): List<ExampleEntity>

    @Insert
    fun insert(entity: ExampleEntity)

    @Query("DELETE FROM {table}")
    fun delete()

    @Query("SELECT WHERE id > {id} FROM {table}")
    fun querySome(id: Int): List<ExampleEntity>

    // 使用TabooLib DSL语句控制
    @DSL
    fun selectUser(name: String): ExampleEntity? {
        return table.workspace(dataSource) {
            select { where { "user" eq name } }
        }.firstOrNull {
            adaptResultSet()
        }
    }
}