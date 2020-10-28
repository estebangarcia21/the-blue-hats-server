package me.stevemmmmm.server.game.enchants;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Test;

import me.stevemmmmm.server.game.utils.LoreBuilder;

public class PeroxideTest {
    @Test
    public void testOnHit() {
        Peroxide enchant = new Peroxide();
        Player damager = mock(Player.class);
        Player damagee = mock(Player.class);

        PlayerInventory inventory = mock(PlayerInventory.class);
        ItemStack item = mock(ItemStack.class);
        ItemMeta meta = mock(ItemMeta.class);

        World world = mock(World.class);

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, DamageCause.ENTITY_ATTACK, 0);

        when(damagee.getInventory()).thenReturn(inventory);
        when(damagee.getWorld()).thenReturn(world);
        when(damager.getWorld()).thenReturn(world);
        when(world.getName()).thenReturn("");

        when(inventory.getLeggings()).thenReturn(item);
        when(item.getType()).thenReturn(Material.LEATHER_LEGGINGS);
        when(item.getItemMeta()).thenReturn(meta);
        when(meta.getLore()).thenReturn(new LoreBuilder().write(ChatColor.BLUE, "Peroxide").build());

        enchant.onHit(event);

        assertEquals(true, enchant.canExecuteEnchant(item, new Entity[] { damager, damagee }));

        Arrow arrow = mock(Arrow.class);
        event = new EntityDamageByEntityEvent(arrow, damagee, DamageCause.ENTITY_ATTACK, 0);

        enchant.onHit(event);

        when(arrow.getShooter()).thenReturn(damager);

        assertEquals(true, enchant.canExecuteEnchant(item, new Entity[] { arrow, damager, damagee }));
    }

    @Test
    public void testRegenerationIsGivenToPlayer() {
        Peroxide enchant = new Peroxide();
        Player player = mock(Player.class);

        enchant.executeEnchant(player, 1, 1);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 1));
    }
}
