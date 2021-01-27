package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ChippingTest : DescribeSpec ({
    describe("chipping") {
        it("does true damage to the damagee on arrow hit") {
            val damageeMock = mockk<Player>()
            val damagerMock = mockk<Player>()

            val damageManagerMock = mockk<DamageManager> {
                every { doTrueDamage(damageeMock, 3.0, damagerMock) } returns Unit
            }

            val chipping = Chipping(damageManagerMock, mockk())

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damagee } returns damageeMock
                every { damager } returns damagerMock
            }

            chipping.execute(data)

            verify { damageManagerMock.doTrueDamage(damageeMock, 3.0, damagerMock)}
        }
    }
})
