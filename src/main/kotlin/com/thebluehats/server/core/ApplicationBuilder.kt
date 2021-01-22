package com.thebluehats.server.core

import com.google.inject.Guice
import com.google.inject.Injector
import com.thebluehats.server.core.modules.*
import com.thebluehats.server.core.services.Service
import java.util.*

class ApplicationBuilder(private val plugin: TheBlueHatsServerPlugin) {
    private val services = ArrayList<Class<out Service?>>()

    fun addService(clazz: Class<out Service?>): ApplicationBuilder {
        services.add(clazz)
        return this
    }

    fun build(): Application {
        val injector = provisionInjector()

        for (service in services) {
            injector.getInstance(service)!!.provision(injector)
        }

        return injector.getInstance(Application::class.java)
    }

    private fun provisionInjector(): Injector {
        return Guice.createInjector(
            PluginModule(plugin), RegionManagerModule(),
            CustomEnchantManagerModule(), CombatManagerModule(), EventVerifiersModule(),
            DamageManagerModule(), BowManagerModule(), TimerModule(), HitCounterModule(),
            MirrorModule(), CustomEnchantUtilsModule(), ServerAPIModule(),
            RomanNumeralConverterModule(), PantsDataContainerModule(),
            PerformanceStatsServiceModule(), GlobalTimerModule(), DamageEnchantTriggersModule(),
            RegistererModule(), PitScoreboardModule()
        )
    }
}