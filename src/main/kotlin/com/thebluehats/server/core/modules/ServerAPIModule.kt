package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.core.modules.annotations.ServerAPI
import kong.unirest.Unirest
import kong.unirest.UnirestInstance

class ServerAPIModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        @ServerAPI
        @JvmStatic
        fun provideServerAPI(): UnirestInstance {
            val apiUrl = System.getenv("API_URL")
            val defaultApiUrl = "http://localhost:4000"

            val instance = Unirest.spawnInstance()
            instance.config().defaultBaseUrl(if (apiUrl.isNullOrEmpty()) defaultApiUrl else apiUrl)
            instance.config().setDefaultHeader("x-api-key", System.getenv("API_KEY"))

            return instance
        }
    }
}