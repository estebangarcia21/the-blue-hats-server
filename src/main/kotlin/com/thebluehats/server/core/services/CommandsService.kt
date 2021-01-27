package com.thebluehats.server.core.services

import com.google.inject.Inject
import com.google.inject.Injector
import com.thebluehats.server.game.commands.*
import org.bukkit.plugin.java.JavaPlugin

class CommandsService @Inject constructor(private val plugin: JavaPlugin) : Service {
    override fun provision(injector: Injector) {
        injector.getInstance(SpawnCommand::class.java).registerCommand(plugin)
        injector.getInstance(EnchantCommand::class.java).registerCommand(plugin)
        injector.getInstance(MysticEnchantsCommand::class.java).registerCommand(plugin)
        injector.getInstance(SelectWorldCommand::class.java).registerCommand(plugin)
        injector.getInstance(SetGoldCommand::class.java).registerCommand(plugin)
        injector.getInstance(UnenchantCommand::class.java).registerCommand(plugin)
        injector.getInstance(AboutCommand::class.java).registerCommand(plugin)
        injector.getInstance(GiveFreshItemCommand::class.java).registerCommand(plugin)
        injector.getInstance(GiveProtCommand::class.java).registerCommand(plugin)
        injector.getInstance(GiveBreadCommand::class.java).registerCommand(plugin)
        injector.getInstance(GiveArrowCommand::class.java).registerCommand(plugin)
        injector.getInstance(GiveObsidianCommand::class.java).registerCommand(plugin)
    }
}