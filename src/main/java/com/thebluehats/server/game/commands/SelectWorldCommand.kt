package com.thebluehats.server.game.commands

import com.google.inject.Inject
import com.thebluehats.server.game.managers.world.WorldSelectionManager
import org.bukkit.entity.Player

class SelectWorldCommand @Inject constructor(private val worldSelectionManager: WorldSelectionManager) : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("selectworld")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Displays the world selection menu.")
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        worldSelectionManager.displaySelectionMenu(player)
    }
}