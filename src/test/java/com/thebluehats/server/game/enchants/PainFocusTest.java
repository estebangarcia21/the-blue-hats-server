package com.thebluehats.server.game.enchants;

import com.thebluehats.server.game.managers.combat.CalculationMode;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger;
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

public class PainFocusTest {
    @Test
    public void AddsMoreDamageBasedOnHealth() {
        DamageManager damageManager = mock(DamageManager.class);
        PainFocus painFocus = new PainFocus(damageManager, mock(PlayerDamageTrigger.class));

        Player damager = mock(Player.class);
        when(damager.getMaxHealth()).thenReturn(20.0);
        when(damager.getHealth()).thenReturn(20.0);

        Player damagee = mock(Player.class);

        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);

        DamageEventEnchantData data = mock(DamageEventEnchantData.class);
        when(data.getLevel()).thenReturn(3);

        when(data.getEvent()).thenReturn(event);
        when(data.getDamagee()).thenReturn(damagee);
        when(data.getDamager()).thenReturn(damager);

        painFocus.execute(data);

        verify(damageManager).addDamage(any(), anyDouble(), any());
    }
}
