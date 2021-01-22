package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.game.utils.RomanNumeralConverter

class RomanNumeralConverterModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        fun provideRomanNumeralConverter(): RomanNumeralConverter {
            return RomanNumeralConverter()
        }
    }
}