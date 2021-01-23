package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class PrickTest : DescribeSpec({
    describe("prick") {
        it("damages the damager on hit") {
            val damagerMock = mockk<Player>()
            val damageeMock = mockk<Player>()
            val eventMock = mockk<EntityDamageByEntityEvent>()

            val damageManagerMock = mockk<DamageManager> {
                every { doTrueDamage(damagerMock, 1.0, damageeMock ) } returns Unit
            }

            val prick = Prick(damageManagerMock, mockk())

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { event } returns eventMock
                every { damager } returns damagerMock
                every { damagee } returns damageeMock
            }

            prick.execute(data)
            
            verify { damageManagerMock.doTrueDamage(damagerMock, 1.0, damageeMock) }
        }
    }
})
