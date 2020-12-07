package com.thebluehats.server.game.managers.combat.templates;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.*;

import com.thebluehats.server.game.enchants.Wasp;
import com.thebluehats.server.game.enchants.args.PotionEffectArgs;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.utils.LoreBuilder;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thebluehats.server.game.managers.combat.BowManager;

public class EventTemplateTest {
    @Test
    public void EventTemplateRunWhenPlayerHitsPlayer() {
        ItemStack item = mock(ItemStack.class);

        ItemMeta meta = mock(ItemMeta.class);
        when(item.getItemMeta()).thenReturn(meta);
        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp").build());

        PlayerInventory inventory = mock(PlayerInventory.class);
        when(inventory.getItemInMainHand()).thenReturn(item);

        World world = mock(World.class);
        when(world.getName()).thenReturn("");

        Player damager = mock(Player.class);
        when(damager.getWorld()).thenReturn(world);

        Player damagee = mock(Player.class);
        when(damagee.getInventory()).thenReturn(inventory);
        when(damagee.getWorld()).thenReturn(world);

        CustomEnchant<PotionEffectArgs> enchant = spy(new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayer() }));
        doNothing().when(enchant).execute(any());

        EventTemplate template = new PlayerHitPlayer();

        template.run(enchant, damager, damagee, PlayerInventory::getItemInMainHand, level -> enchant.execute(mock(PotionEffectArgs.class)));
        verify(enchant).execute(any());
    }

    @Test
    public void EventTemplateRunWhenArrowHitsPlayer() {
        ItemStack item = mock(ItemStack.class);

        ItemMeta meta = mock(ItemMeta.class);
        when(item.getItemMeta()).thenReturn(meta);
        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Wasp").build());

        PlayerInventory inventory = mock(PlayerInventory.class);
        when(inventory.getItemInMainHand()).thenReturn(item);

        World world = mock(World.class);
        when(world.getName()).thenReturn("");

        Player damager = mock(Player.class);
        when(damager.getWorld()).thenReturn(world);

        Player damagee = mock(Player.class);
        when(damagee.getInventory()).thenReturn(inventory);
        when(damagee.getWorld()).thenReturn(world);

        Arrow arrow = mock(Arrow.class);
        when(arrow.getShooter()).thenReturn(damager);

        CustomEnchant<PotionEffectArgs> enchant = spy(new Wasp(new BowManager(), new EventTemplate[] { new ArrowHitPlayer() }));
        doNothing().when(enchant).execute(any());

        EventTemplate template = new ArrowHitPlayer();

        template.run(enchant, arrow, damagee, PlayerInventory::getItemInMainHand, level -> enchant.execute(mock(PotionEffectArgs.class)));
        verify(enchant).execute(any());
    }
}
