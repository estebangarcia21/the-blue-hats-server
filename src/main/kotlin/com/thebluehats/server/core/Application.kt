package com.thebluehats.server.core

import com.google.inject.Inject
import com.thebluehats.server.game.utils.PluginLifecycleListener
import org.bukkit.Bukkit
import java.util.function.Consumer

class Application @Inject constructor(private val plugin: TheBlueHatsServerPlugin) {
    fun start() {
        plugin.lifecycleListeners.forEach { l -> l.onPluginStart() }

        logInitializationMessage()
    }

    fun stop() {
        plugin.lifecycleListeners.forEach { l -> l.onPluginEnd() }
    }

    private fun logInitializationMessage() {
        Bukkit.getLogger().info(
            """
  _______ _            ____  _              _    _       _          _____                          
 |__   __| |          |  _ \| |            | |  | |     | |        / ____|                         
    | |  | |__   ___  | |_) | |_   _  ___  | |__| | __ _| |_ ___  | (___   ___ _ ____   _____ _ __ 
    | |  | '_ \ / _ \ |  _ <| | | | |/ _ \ |  __  |/ _` | __/ __|  \___ \ / _ \ '__\ \ / / _ \ '__|
    | |  | | | |  __/ | |_) | | |_| |  __/ | |  | | (_| | |_\__ \  ____) |  __/ |   \ V /  __/ |   
    |_|  |_| |_|\___| |____/|_|\__,_|\___| |_|  |_|\__,_|\__|___/ |_____/ \___|_|    \_/ \___|_|   

   ___        ___ _                                             
  | _ )_  _  / __| |_ _____ _____ _ __  _ __  _ __  _ __  _ __  
  | _ \ || | \__ \  _/ -_) V / -_) '  \| '  \| '  \| '  \| '  \ 
  |___/\_, | |___/\__\___|\_/\___|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|
       |__/                                                     
"""
        )
    }
}