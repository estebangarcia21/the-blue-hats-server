package com.thebluehats.server.game.commands;

import java.util.UUID;

import com.google.inject.Inject;
import com.thebluehats.server.api.models.PitDataModel;
import com.thebluehats.server.api.repos.Repository;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class SetGoldCommand extends GameCommand {
    // TODO Update repository scope
    private final Repository<UUID, PitDataModel> pitDataRepository;

    @Inject
    public SetGoldCommand(Repository<UUID, PitDataModel> pitDataRepository) {
        this.pitDataRepository = pitDataRepository;
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

            pitDataRepository.update(player.getUniqueId(), model -> model.setGold(gold));
        } else {
            player.sendMessage(getUsageMessage(commandName));
        }
    }
}
