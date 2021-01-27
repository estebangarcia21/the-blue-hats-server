package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.event.entity.EntityDamageByEntityEvent

class RingArmorTest : DescribeSpec({
    describe("ring armor") {
        it("should reduce damage done by arrows") {
            val eventMock = mockk<EntityDamageByEntityEvent>()
            val damageManagerMock = mockk<DamageManager> {
                every { reduceDamageByPercentage(eventMock, .60) } returns Unit
            }

            val ringArmor = RingArmor(damageManagerMock, mockk())

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { event } returns eventMock
            }

            ringArmor.execute(data)

            verify{ damageManagerMock.reduceDamageByPercentage(eventMock, .60)}
        }
    }
})