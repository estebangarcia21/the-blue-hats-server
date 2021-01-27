package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class BruiserTest : DescribeSpec({
    describe("bruiser") {
        it("reduces damage when blocking") {
            val damageManagerMock: DamageManager = mockk {
                every { addHeartDamageReduction(any(), any()) } returns Unit
            }

            val bruiser = Bruiser(damageManagerMock, mockk(), mockk())

            val damageeMock: Player = mockk {
                every { isBlocking } returns true
            }
            val eventMock: EntityDamageByEntityEvent = mockk()

            val data: DamageEventEnchantData = mockk {
                every { level } returns 3
                every { event } returns eventMock
                every { damagee } returns damageeMock
            }

            bruiser.execute(data)

            verify { damageManagerMock.addHeartDamageReduction(eventMock, 4) }
        }

        it("does not reduce damage when not blocking") {
            val damageManagerMock: DamageManager = mockk {
                every { addHeartDamageReduction(any(), any()) } returns Unit
            }

            val bruiser = Bruiser(damageManagerMock, mockk(), mockk())

            val damageeMock: Player = mockk {
                every { isBlocking } returns false
            }
            val eventMock: EntityDamageByEntityEvent = mockk()

            val data: DamageEventEnchantData = mockk {
                every { level } returns 3
                every { event } returns eventMock
                every { damagee } returns damageeMock
            }

            bruiser.execute(data)

            verify(inverse = true) { damageManagerMock.addHeartDamageReduction(eventMock, 4) }
        }
    }
})
