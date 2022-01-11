package kim.bifrost.rain.orm.example

import kim.bifrost.rain.orm.api.ORMBuilder

/**
 * kim.bifrost.rain.orm.example.AppDatabase
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 21:48
 **/
object AppDatabase {
    private val builder by lazy {
        ORMBuilder.newBuilder()
            .database("bifrost")
            .user("root")
            .password("123456")
            .port(3306)
            .host("localhost")
            .buildHost()
    }

    val exampleDao by lazy { builder.build("rain_orm", ExampleDao::class.java) }
}