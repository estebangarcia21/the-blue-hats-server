package com.thebluehats.server.core.modules

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.thebluehats.server.core.modules.annotations.MirrorReference
import com.thebluehats.server.game.enchants.Mirror

class MirrorModule : AbstractModule() {
    override fun configure() {}

    companion object {
        @Provides
        @Singleton
        @MirrorReference
        @JvmStatic
        fun provideMirror(): Mirror {
            return Mirror()
        }
    }
}