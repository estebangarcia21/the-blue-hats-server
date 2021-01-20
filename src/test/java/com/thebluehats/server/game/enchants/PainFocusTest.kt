package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import org.mockito.Mockito
import com.thebluehats.server.game.enchants.PainFocus
import com.thebluehats.server.game.managers.combat.templates.PlayerDamageTrigger
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import com.thebluehats.server.game.managers.combat.CalculationMode
import org.junit.Test
import org.mockito.Matchers

class PainFocusTest {
    @Test
    fun AddsMoreDamageBasedOnHealth() {
        val damageManager = Mockito.mock(DamageManager::class.java)
        val painFocus = PainFocus(damageManager, Mockito.mock(PlayerDamageTrigger::class.java))
        val damager = Mockito.mock(Player::class.java)
        Mockito.`when`(damager.maxHealth).thenReturn(20.0)
        Mockito.`when`(damager.health).thenReturn(20.0)
        val damagee = Mockito.mock(Player::class.java)
        val event = Mockito.mock(EntityDamageByEntityEvent::class.java)
        val data = Mockito.mock(DamageEventEnchantData::class.java)
        Mockito.`when`(data.level).thenReturn(3)
        Mockito.`when`(data.event).thenReturn(event)
        Mockito.`when`(data.damagee).thenReturn(damagee)
        Mockito.`when`(data.damager).thenReturn(damager)
        painFocus.execute(data)
        Mockito.verify(damageManager).addDamage(Matchers.any(), Matchers.anyDouble(), Matchers.any())
    }
}