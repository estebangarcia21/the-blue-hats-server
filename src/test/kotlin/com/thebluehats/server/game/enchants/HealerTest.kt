package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player

class HealerTest : DescribeSpec({
    describe("healer") {
        it("heals both players") {
            val healer = Healer(mockk())

            val damager = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 0.0
            }
            val damagee = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 0.0
            }

            val data = mockk<DamageEventEnchantData> {
                every { damager } returns damager
                every { damagee } returns damagee
                every { level } returns 3
            }

            healer.execute(data)

            verify { damager.health = 6.0 }
            verify { damagee.health = 6.0 }
        }
    }
})
