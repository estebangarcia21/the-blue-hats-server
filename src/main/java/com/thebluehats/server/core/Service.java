package com.thebluehats.server.core;

import com.google.inject.Injector;

public interface Service {
    void provision(Injector injector);
}
