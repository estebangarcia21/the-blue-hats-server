package me.stevemmmmm.server.game.enchants;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.stevemmmmm.server.game.utils.LoreBuilder;

public class ComboSwift extends CustomEnchant {
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(4, 3, 3);
    private final EnchantProperty<Integer> speedTime = new EnchantProperty<>(3, 4, 5);
    private final EnchantProperty<Integer> speedAmplifier = new EnchantProperty<>(0, 1, 1);

    private HitCounter hitCounter;

    public ComboSwift(HitCounter hitCounter) {
        this.hitCounter = hitCounter;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            ItemStack source = damager.getInventory().getItemInMainHand();

            if (!canExecuteEnchant(source, new Entity[] { damager }))
                return;

            int level = getEnchantLevel(source);

            executeEnchant(damager, hitsNeeded.getValueAtLevel(level), speedTime.getValueAtLevel(level),
                    speedAmplifier.getValueAtLevel(level));
        }
    }

    public void executeEnchant(Player player, int hitsNeeded, int duration, int amplifier) {
        hitCounter.addOne(player);

        if (hitCounter.hasHits(player, hitsNeeded)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * 20, amplifier, true));
        }
    }

    @Override
    public String getName() {
        return "Combo: Swift";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Comboswift";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return new LoreBuilder().declareVariable("fourth", "third", "third")
                .declareVariable("Speed I", "Speed II", "Speed III").declareVariable("3", "4", "5").write("Every ")
                .writeVariable(ChatColor.YELLOW, 0, level).write(" strike gain").next()
                .writeVariable(ChatColor.YELLOW, 1, level).write(" (").writeVariable(2, level).write("s)").build();
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
        return new Material[] { Material.GOLDEN_SWORD };
    }
}
