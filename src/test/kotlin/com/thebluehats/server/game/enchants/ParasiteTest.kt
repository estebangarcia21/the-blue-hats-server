package com.thebluehats.server.game.enchants


import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.bukkit.entity.Player

class ParasiteTest : DescribeSpec({
    describe("parasite") {
        it("works") {
            val parasite = Parasite(mockk())

            val mp = mockk<Player> {
                every { health }.returnsMany(5.0)
            }

            mp.health shouldBe 5.0
        }
    }
})
