package com.thebluehats.server.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.thebluehats.server.game.utils.RomanNumeralConverter;

public class RomanNumeralConverterModule extends AbstractModule {
    @Provides
    @Singleton
    static RomanNumeralConverter provideRomanNumeralConverter() {
        return new RomanNumeralConverter();
    }

    @Override
    protected void configure() {
    }
}
