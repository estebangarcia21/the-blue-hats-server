package com.thebluehats.server.game.other;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class Bread implements Listener {
    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.BREAD) {
            event.getPlayer().setHealth(Math.min(event.getPlayer().getMaxHealth(), event.getPlayer().getHealth() + 8));

            EntityPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
            player.setAbsorptionHearts(Math.min(player.getAbsorptionHearts() + 2, 4));
        }
    }
}