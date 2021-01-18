package com.thebluehats.server.api.listeners;

import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import org.bukkit.event.Listener;

public abstract class APIListener implements Listener {
    protected static final String API_KEY = System.getenv("API_KEY");

    protected HttpRequest<HttpRequestWithBody> standardRequest(HttpRequest<HttpRequestWithBody> request) {
        request.queryString("key", API_KEY);

        return request;
    }
}
