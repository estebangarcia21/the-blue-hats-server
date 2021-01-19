package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PainFocusTest {
    @Test
    public void AddsMoreDamageBasedOnHealth() {
        DamageManager damageManager = mock(DamageManager.class);
        PainFocus painFocus = new PainFocus(damageManager, mock(PlayerDamageTrigger.class));

        Player damager = mock(Player.class);
        doReturn(20.0).when(damager).getMaxHealth();
//        when(damager.getMaxHealth()).thenReturn(20.0);
        when(damager.getHealth()).thenReturn(20.0);

        Player damagee = mock(Player.class);
        when(damagee.getMaxHealth()).thenReturn(20.0);
        when(damagee.getHealth()).thenReturn(20.0);

        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);

        DamageEventEnchantData data = mock(DamageEventEnchantData.class);
        when(data.getLevel()).thenReturn(3);

        when(data.getEvent()).thenReturn(event);
        when(data.getDamagee()).thenReturn(damagee);
        when(data.getDamager()).thenReturn(damager);

        painFocus.execute(data);

        verify(damageManager).addDamage(event, 0.25D, CalculationMode.ADDITIVE);
    }
}
