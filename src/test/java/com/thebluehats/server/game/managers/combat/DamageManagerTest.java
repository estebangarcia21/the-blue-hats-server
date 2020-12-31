package com.thebluehats.server.game.managers.combat;

import com.thebluehats.server.game.enchants.Mirror;
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils;
import com.thebluehats.server.game.managers.world.regionmanager.RegionManager;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DamageManagerTest {
    @Test
    public void CorrectlyAddsAdditiveDamage() {
        DamageManager damageManager = new DamageManager(mock(Mirror.class), mock(CombatManager.class), mock(CustomEnchantUtils.class), mock(RegionManager.class));

        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);
        Entity damager = mock(Entity.class);

        when(event.getDamage()).thenReturn(7.5);
        when(event.getDamager()).thenReturn(damager);
        when(damager.getUniqueId()).thenReturn(UUID.randomUUID());

        damageManager.addDamage(event, 0.25, CalculationMode.ADDITIVE);
        damageManager.addDamage(event, 0.25, CalculationMode.ADDITIVE);

        assertEquals(11.25D, damageManager.getDamageFromEvent(event), 0.1);
    }

    @Test
    public void CorrectlyAddsMultiplicativeDamage() {
        DamageManager damageManager = new DamageManager(mock(Mirror.class), mock(CombatManager.class), mock(CustomEnchantUtils.class), mock(RegionManager.class));

        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);
        Entity damager = mock(Entity.class);

        when(event.getDamage()).thenReturn(5D);
        when(event.getDamager()).thenReturn(damager);
        when(damager.getUniqueId()).thenReturn(UUID.randomUUID());

        damageManager.addDamage(event, 1, CalculationMode.MULTIPLICATIVE);

        assertEquals(10D, damageManager.getDamageFromEvent(event), 0.1);
    }

    @Test
    public void CorrectlyAddsMultiplicativeAndAdditiveDamage() {
        DamageManager damageManager = new DamageManager(mock(Mirror.class), mock(CombatManager.class), mock(CustomEnchantUtils.class), mock(RegionManager.class));

        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);
        Entity damager = mock(Entity.class);

        when(event.getDamage()).thenReturn(1D);
        when(event.getDamager()).thenReturn(damager);
        when(damager.getUniqueId()).thenReturn(UUID.randomUUID());

        damageManager.addDamage(event, 0.25, CalculationMode.ADDITIVE);
        damageManager.addDamage(event, 1, CalculationMode.MULTIPLICATIVE);

        assertEquals(2.5D, damageManager.getDamageFromEvent(event), 0.1);
    }
}
