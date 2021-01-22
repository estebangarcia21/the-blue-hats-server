package com.thebluehats.server.game.enchants


import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.bukkit.Material
import org.bukkit.entity.Player

class ParasiteTest : DescribeSpec({
    describe("parasite") {
        it("adds health to the bow shooter on arrow hit") {
            val parasite = Parasite(mockk())

            val player = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 5.0
                every { health = any() } returns Unit
            }

            val data = DamageEventEnchantData(mockk(), player, mockk(), hashMapOf(Material.BOW to 3))

            parasite.execute(data)

            verify { player.health = 7.0 }
        }
    }
})
