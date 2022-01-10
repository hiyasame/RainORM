package kim.bifrost.rain.orm.api

/**
 * kim.bifrost.rain.orm.api.PrimaryKey
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 2:01
 **/
@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey(val autoGenerate: Boolean = false)
