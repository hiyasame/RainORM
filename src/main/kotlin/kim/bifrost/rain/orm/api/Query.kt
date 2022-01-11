package kim.bifrost.rain.orm.api

/**
 * kim.bifrost.rain.orm.api.Query
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 2:09
 **/
@Target(AnnotationTarget.FUNCTION)
annotation class Query(val sql: String)
