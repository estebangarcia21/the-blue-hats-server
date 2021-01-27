package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player

class HealerTest : DescribeSpec({
    describe("healer") {
        it("heals the damager and damagee on hit") {
            val healer = Healer(mockk())

            val damagerMock = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 5.0
                every { health = any() } returns Unit
            }

            val damageeMock = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 5.0
                every { health = any() } returns Unit
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damager } returns damagerMock
                every { damagee } returns damageeMock
            }

            healer.execute(data)

            verify { damagerMock.health = 11.0 }
            verify { damageeMock.health = 11.0 }
        }
    }
})
