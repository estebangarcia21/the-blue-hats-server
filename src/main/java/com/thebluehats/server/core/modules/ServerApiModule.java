package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.core.modules.annotations.ServerApi;

import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;

public class ServerApiModule extends AbstractModule {
    @Provides
    @Singleton
    @ServerApi
    static UnirestInstance provideServerApi() {
        String apiUrl = System.getenv("API_URL");
        String defaultApiUrl = "http://localhost:3000/api";

        if (apiUrl != null) {
            if (apiUrl.isEmpty()) {
                apiUrl = defaultApiUrl;
            }
        } else {
            apiUrl = defaultApiUrl;
        }

        UnirestInstance instance = Unirest.spawnInstance();
        instance.config().defaultBaseUrl(apiUrl);

        return instance;
    }

    @Override
    protected void configure() {
    }
}