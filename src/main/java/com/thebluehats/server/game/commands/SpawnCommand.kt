package com.thebluehats.server.game.commands

import com.google.inject.Inject
import com.thebluehats.server.game.managers.combat.CombatManager
import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SpawnCommand @Inject constructor(
    private val combatManager: CombatManager,
    private val timer: Timer<Player>,
    private val regionManager: RegionManager
) : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("spawn")

    override fun getUsageMessage(cmd: String?): String? {
        return null
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        if (regionManager.entityIsInSpawn(player)) {
            player.sendMessage(ChatColor.RED.toString() + "You cannot /respawn here!")
            return
        }
        if (!combatManager.playerIsInCombat(player)) {
            if (!timer.isRunning(player)) {
                player.health = player.maxHealth
                player.teleport(regionManager.getSpawnLocation(player))
                timer.start(player, 200, false)
            } else {
                player.sendMessage(ChatColor.RED.toString() + "You may only /respawn every 10 seconds")
            }
        } else {
            player.sendMessage(
                ChatColor.RED.toString() + ChatColor.BOLD + "HOLD UP! " + ChatColor.GRAY
                        + "Can't /respawn while fighting (" + ChatColor.RED + combatManager.getCombatTime(player) + "s"
                        + ChatColor.GRAY + " left)"
            )
        }
    }
}