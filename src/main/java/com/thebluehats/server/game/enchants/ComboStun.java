package com.thebluehats.server.game.enchants;

import com.google.inject.Inject;
import com.thebluehats.server.game.managers.combat.templates.DamageEnchantTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant;
import com.thebluehats.server.game.managers.enchants.EnchantGroup;
import com.thebluehats.server.game.managers.enchants.EnchantProperty;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import com.thebluehats.server.game.utils.EnchantLoreParser;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class ComboStun extends DamageTriggeredEnchant {
    private final EnchantProperty<Integer> duration = new EnchantProperty<>(10, 16, 30);
    private final EnchantProperty<Integer> hitsNeeded = new EnchantProperty<>(5, 4, 4);

    private final HitCounter hitCounter;

    @Inject
    public ComboStun(HitCounter hitCounter, PlayerDamageTrigger playerDamageTrigger) {
        super(new DamageEnchantTrigger[] { playerDamageTrigger });

        this.hitCounter = hitCounter;
    }

    @Override
    public void execute(DamageEventEnchantData data) {
        Player damager = data.getDamager();
        Player damagee = data.getDamagee();
        int level = data.getLevel();

        hitCounter.addOne(damager);

        if (hitCounter.hasHits(damager, hitsNeeded.getValueAtLevel(level))) {
            int durationTime = duration.getValueAtLevel(level);

            damagee.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, durationTime, 8), true);
            damagee.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, durationTime, -8), true);
            damagee.getWorld().playSound(damagee.getLocation(), Sound.ANVIL_LAND, 1, 0.1f);

            sendPackets(damagee);
        }
    }

    private void sendPackets(Player player) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a(
                "{\"text\": \"" + ChatColor.RED + "STUNNED!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(0, 60, 0);

        IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.YELLOW
                + "You cannot move!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

        PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
        PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(0, 60, 0);

        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        playerConnection.sendPacket(title);
        playerConnection.sendPacket(length);
        playerConnection.sendPacket(subTitle);
        playerConnection.sendPacket(subTitleLength);
    }

    @Override
    public String getName() {
        return "Combo: Stun";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Combostun";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        EnchantLoreParser enchantLoreParser = new EnchantLoreParser( "Every <yellow>{0}</yellow> strike on an enemy<br/>stuns them for {1} seconds");

        String[][] variables = new String[2][];
        variables[0] = new String[] { "fifth", "fourth", "fourth" };
        variables[1] = new String[] { "0.5", "0.8", "1.5" };

        enchantLoreParser.setVariables(variables);

        return enchantLoreParser.parseForLevel(level);
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
        return true;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[] { Material.GOLD_SWORD };
    }

    @Override
    public EnchantHolder getEnchantHolder() {
        return EnchantHolder.DAMAGER;
    }
}
