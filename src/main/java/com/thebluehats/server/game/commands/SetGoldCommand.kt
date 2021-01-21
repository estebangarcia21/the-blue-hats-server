package com.thebluehats.server.game.commands

import com.google.inject.Inject
import com.thebluehats.server.api.daos.PerformanceStatsService
import org.apache.commons.lang.StringUtils
import org.bukkit.entity.Player

class SetGoldCommand @Inject constructor(private val performanceStatsService: PerformanceStatsService) : GameCommand() {
    override val commandNames: Array<String>
        get() = arrayOf("setgold")

    override fun getUsageMessage(cmd: String?): String? {
        return formatStandardUsageMessage(cmd!!, "Sets your gold amount.", "amount")
    }

    override fun runCommand(player: Player, cmd: String?, args: Array<String>) {
        if (StringUtils.isNumeric(args[0])) {
            val gold = Math.min(args[0].toDouble(), 1000000000.0)
            performanceStatsService.setPlayerGold(player, gold)
        } else {
            player.sendMessage(getUsageMessage(cmd))
        }
    }
}