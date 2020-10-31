package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import me.stevemmmmm.server.game.enchants.templates.EventTemplate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.stevemmmmm.server.game.enchants.templates.ArrowHitPlayer;
import me.stevemmmmm.server.game.utils.LoreBuilder;

public class SprintDrain extends CustomEnchant {
    private final EnchantProperty<Integer> speedDuration = new EnchantProperty<>(5, 5, 7);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 0, 1);

    public SprintDrain(EventTemplate... templates) {
        super(templates);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        runEventTemplates(this, event.getDamager(), event.getEntity(), PlayerInventory::getItemInMainHand,
                level -> executeEnchant((Player) event.getDamager(), (Player) event.getEntity(),
                        speedDuration.getValueAtLevel(level), speedAmplifier.getValueAtLevel(level), level));
    }

    public void executeEnchant(Player damager, Player damaged, int speedDuration, int speedAmplifier, int level) {
        damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration * 20, speedAmplifier));

        if (level == 1)
            return;

        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0));
    }

    @Override
    public String getName() {
        return "Sprint Drain";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Sprintdrain";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("I", "I", "II").declareVariable("", "5", "7")
                .write("Arrow shots grant you ").write(ChatColor.YELLOW, "Speed ")
                .writeVariable(ChatColor.YELLOW, 0, level).next().write("(").writeVariable(1, level).write("s)")
                .setWriteCondition(level != 1).write(" and apply ").write(ChatColor.BLUE, "Slowness I").next()
                .write("(3s)").build();
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return EnchantGroup.C;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.BOW };
    }
}
