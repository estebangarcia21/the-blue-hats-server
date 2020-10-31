package me.stevemmmmm.server.game.enchants;

import me.stevemmmmm.server.game.enchants.templates.EventTemplate;
import me.stevemmmmm.server.game.utils.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class LastStand extends CustomEnchant {
    private final EnchantProperty<Integer> effectAmplifier = new EnchantProperty<>(0, 1, 2);

    public LastStand(EventTemplate... templates) {
        super(templates);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getLeggings,
                level -> executeEnchant((Player) event.getEntity(), effectAmplifier.getValueAtLevel(level)));
    }

    public void executeEnchant(Player damaged, int effectAmplifier) {
        if (damaged.getHealth() < 10)
            damaged.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, effectAmplifier, true));
    }

    @Override
    public String getName() {
        return "Last Stand";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Laststand";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("I", "II", "III").write("Gain ").setColor(ChatColor.BLUE)
                .write("Resistance ").writeVariable(0, level).resetColor().write(" (4").next()
                .write("seconds) when reaching ").setColor(ChatColor.RED).write("3❤").build();
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
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.LEATHER_LEGGINGS };
    }
}