package com.thebluehats.server.core.services;

import com.google.inject.Injector;

public interface Service {
    void provision(Injector injector);
}
