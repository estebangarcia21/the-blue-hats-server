package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.stevemmmmm.server.game.enchants.templates.PlayerHitPlayer;
import me.stevemmmmm.server.game.utils.LoreBuilder;
import org.bukkit.inventory.PlayerInventory;

public class Healer extends CustomEnchant {
    private final EnchantProperty<Integer> healAmount = new EnchantProperty<>(2, 4, 6);

    public Healer() {
        super(new PlayerHitPlayer());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        getEventTemplates()[0].run(this, event.getDamager(), event.getEntity(),
                PlayerInventory::getItemInMainHand, level -> executeEnchant((Player) event.getDamager(),
                        (Player) event.getEntity(), healAmount.getValueAtLevel(level)));
    }

    public void executeEnchant(Player damager, Player damaged, int healAmount) {
        damager.setHealth(Math.min(damager.getHealth() + healAmount,
                Objects.requireNonNull(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()));
        damaged.setHealth(Math.min(damaged.getHealth() + healAmount,
                Objects.requireNonNull(damaged.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()));
    }

    @Override
    public String getName() {
        return "Healer";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Healer";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("1❤", "2❤", "3❤").write("Hitting a player ").setColor(ChatColor.GREEN)
                .write("heals").resetColor().write(" both you and them for ").setColor(ChatColor.RED)
                .writeVariable(0, level).build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return true;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLDEN_SWORD };
    }
}