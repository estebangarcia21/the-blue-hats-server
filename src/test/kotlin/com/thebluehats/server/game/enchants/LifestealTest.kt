package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class LifestealTest : DescribeSpec({
    describe("lifesteal") {
        it("heals the damager for a certain amount of the damage from an event, capping at 1.5 hearts") {
            val eventMock = mockk<EntityDamageByEntityEvent>()
            val damageManagerMock = mockk<DamageManager>{
                every { getDamageFromEvent(eventMock) } returns 50.0
            }

            val lifesteal = Lifesteal(damageManagerMock, mockk())

            val damagerMock = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 10.0
                every { health = any() } returns Unit
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damager } returns damagerMock
                every { event } returns eventMock
            }

            lifesteal.execute(data)

            verify { damagerMock.health = 13.0 }
        }
    }
})
