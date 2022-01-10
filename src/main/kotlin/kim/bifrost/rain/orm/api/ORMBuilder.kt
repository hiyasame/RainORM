package kim.bifrost.rain.orm.api

import kim.bifrost.rain.orm.api.interfaces.IORMBuilder
import kim.bifrost.rain.orm.api.interfaces.PlatformImplementation
import kotlin.reflect.KClass

/**
 * kim.bifrost.rain.orm.api.ORMBuilder
 * RainORM
 *
 * @author 寒雨
 * @since 2022/1/11 2:38
 **/
class ORMBuilder(platform: PlatformImplementation): IORMBuilder by platform.orm {

    companion object {
        fun <T : PlatformImplementation> newBuilder(clazz: KClass<T>): ORMBuilder {
            return ORMBuilder(clazz.java.getConstructor().newInstance())
        }
    }
}