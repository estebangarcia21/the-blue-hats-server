package com.thebluehats.server.game.enchants


import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player

class ParasiteTest : DescribeSpec({
    describe("parasite") {
        it("adds health to the damager on arrow hit") {
            val parasite = Parasite(mockk())

            val mp = mockk<Player> {
                every { maxHealth } returnsMany listOf(20.0)
                every { health } returnsMany listOf(5.0)
                every { health = any() } returnsMany listOf(Unit)
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returnsMany listOf (3)
                every { damager } returnsMany listOf (mp)
            }

            parasite.execute(data)

            verify { mp.health = 7.0 }
        }
    }
})
