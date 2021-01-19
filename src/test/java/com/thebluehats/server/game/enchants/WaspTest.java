package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.templates.ArrowDamageTrigger;
import com.thebluehats.server.game.managers.combat.templates.EnchantHolder;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WaspTest {
    @Test
    public void AppliesWeaknessOnHit() {
        Wasp wasp = new Wasp(mock(BowManager.class), mock(ArrowDamageTrigger.class));

        Player damagee = mock(Player.class);

        DamageEventEnchantData damageEventEnchantData = mock(DamageEventEnchantData.class);
        when(damageEventEnchantData.getLevel()).thenReturn(1);
        when(damageEventEnchantData.getDamagee()).thenReturn(damagee);

        wasp.execute(damageEventEnchantData);

        verify(damagee).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 6 * 20, 1), true);
    }

    @Test
    public void SetsProperLoreForLevel1() {
        Wasp wasp = new Wasp(mock(BowManager.class), mock(ArrowDamageTrigger.class));

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Apply " + ChatColor.RED + "Weakness II" + ChatColor.GRAY + " (6s) on hit");

        ArrayList<String> actualLore = wasp.getDescription(1);

        assertEquals(expectedLore, actualLore);
    }

    @Test
    public void SetsProperLoreForLevel2() {
        Wasp wasp = new Wasp(mock(BowManager.class), mock(ArrowDamageTrigger.class));

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Apply " + ChatColor.RED + "Weakness III" + ChatColor.GRAY + " (11s) on hit");

        ArrayList<String> actualLore = wasp.getDescription(2);

        assertEquals(expectedLore, actualLore);
    }

    @Test
    public void SetsProperLoreForLevel3() {
        Wasp wasp = new Wasp(mock(BowManager.class), mock(ArrowDamageTrigger.class));

        ArrayList<String> expectedLore = new ArrayList<>();
        expectedLore.add(ChatColor.GRAY + "Apply " + ChatColor.RED + "Weakness IV" + ChatColor.GRAY + " (16s) on hit");

        ArrayList<String> actualLore = wasp.getDescription(3);

        assertEquals(expectedLore, actualLore);
    }

    @Test
    public void EnchantHolderIsDamager() {
        Wasp wasp = new Wasp(mock(BowManager.class), mock(ArrowDamageTrigger.class));

        assertEquals(EnchantHolder.DAMAGER, wasp.getEnchantHolder());
    }
}
