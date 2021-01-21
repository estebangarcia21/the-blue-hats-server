package com.thebluehats.server.core.services

import com.google.inject.Injector

interface Service {
    fun provision(injector: Injector)
}