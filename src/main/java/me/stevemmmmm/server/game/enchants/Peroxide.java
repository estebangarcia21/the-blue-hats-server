package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.stevemmmmm.server.game.enchants.templates.EventTemplate;
import me.stevemmmmm.server.game.utils.LoreBuilder;

public class Peroxide extends CustomEnchant {
    private final EnchantProperty<Integer> regenTime = new EnchantProperty<>(5, 8, 8);
    private final EnchantProperty<Integer> effectAmplifier = new EnchantProperty<>(0, 0, 1);

    public Peroxide(EventTemplate... templates) {
        super(templates);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getLeggings,
                level -> executeEnchant((Player) event.getEntity(), regenTime.getValueAtLevel(level),
                        effectAmplifier.getValueAtLevel(level)));
    }

    public void executeEnchant(Player hitPlayer, int regenTime, int effectAmplifier) {
        hitPlayer.addPotionEffect(
                new PotionEffect(PotionEffectType.REGENERATION, regenTime * 20, effectAmplifier, true));
    }

    @Override
    public String getName() {
        return "Peroxide";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Peroxide";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("Regen I", "Regen I", "Regen II").declareVariable("5", "8", "8")
                .write("Gain ").writeVariable(ChatColor.RED, 0, level).write(" (").writeVariable(1, level).write("s)")
                .write(" when hit").build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.B;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}