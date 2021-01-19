package com.thebluehats.server.game.commands;

import com.google.inject.Inject;
import com.thebluehats.server.api.daos.PerformanceStatsService;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class SetGoldCommand extends GameCommand {
    private final PerformanceStatsService performanceStatsService;

    @Inject
    public SetGoldCommand(PerformanceStatsService performanceStatsService) {
        this.performanceStatsService = performanceStatsService;
    }

    @Override
    public String[] getCommandNames() {
        return new String[] { "setgold" };
    }

    @Override
    public String getUsageMessage(String cmd) {
        return formatStandardUsageMessage(cmd, "Sets your gold amount.", "amount");
    }

    @Override
    public void runCommand(Player player, String commandName, String[] args) {
        if (StringUtils.isNumeric(args[0])) {
            double gold = Math.min(Double.parseDouble(args[0]), 1000000000);

            performanceStatsService.setPlayerGold(player, gold);
        } else {
            player.sendMessage(getUsageMessage(commandName));
        }
    }
}
