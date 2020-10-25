package me.stevemmmmm.server.game.managers;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BowManager {
    private final HashMap<Arrow, PlayerInventory> data = new HashMap<>();

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {
        if (event.getProjectile() instanceof Arrow) {
            if (((Arrow) event.getProjectile()).getShooter() instanceof Player) {
                data.put((Arrow) event.getProjectile(),
                        ((Player) ((Arrow) event.getProjectile()).getShooter()).getInventory());
            }
        }
    }

    public void registerArrow(Arrow arrow, Player player) {
        data.put(arrow, player.getInventory());
    }

    public ItemStack getBowFromArrow(Arrow arrow) {
        for (Arrow arr : data.keySet()) {
            if (arr.equals(arrow)) {
                return data.get(arr).getItemInMainHand();
            }
        }

        return new ItemStack(Material.BOW);
    }

    public ItemStack getLeggingsFromArrow(Arrow arrow) {
        return data.get(arrow).getLeggings();
    }

    public int add(int a, int b) {
        return a + b;
    }
}
