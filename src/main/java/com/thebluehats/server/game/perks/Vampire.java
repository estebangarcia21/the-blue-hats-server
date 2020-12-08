package com.thebluehats.server.game.perks;

import com.google.inject.Inject;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.thebluehats.server.game.managers.combat.DamageManager;

public class Vampire extends Perk {
    private DamageManager damageManager;

    @Inject
    public Vampire(DamageManager damageManager) {
        this.damageManager = damageManager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        int healthValue = 1;

        Player player;
        switch (event.getDamager().getType()) {
            case PLAYER:
                if (!(event.getDamager() instanceof Player))
                    return;

                player = (Player) event.getDamager();

                healthValue = 1;
                break;
            case ARROW:
                Arrow arrow = (Arrow) event.getDamager();

                if (!(arrow.getShooter() instanceof Player))
                    return;

                player = (Player) arrow.getShooter();

                healthValue = arrow.isCritical() ? 3 : 1;
                break;
            default:
                return;
        }

        if (!damageManager.playerIsInCanceledEvent(player))
            return;

        player.setHealth(Math.min(player.getHealth() + healthValue,
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }

    @Override
    public String getName() {
        return "Vampire";
    }

    @Override
    public int getCost() {
        return 4000;
    }
}
