package kim.bifrost.rain.orm

import kim.bifrost.rain.orm.example.AppDatabase
import kim.bifrost.rain.orm.example.ExampleEntity
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit

object RainORM : Plugin() {

    override fun onEnable() {
        submit(async = true) {
            AppDatabase.exampleDao.insert(ExampleEntity(null, "MESSAGE", "ColdRain_Moro", "阿巴阿巴"))
            info("create OK!")
        }
    }
}