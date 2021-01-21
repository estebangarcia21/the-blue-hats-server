package com.thebluehats.server.game.utils

import com.thebluehats.server.game.utils.PlayerHealth.Utils.addHealth
import com.thebluehats.server.game.utils.PlayerHealth.Utils.removeHealth
import com.thebluehats.server.game.utils.PlayerHealth.Utils.setHealth
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.bukkit.entity.Player

class PlayerHealthTest : DescribeSpec({
    describe("player health utils") {
        it("adds player health and caps it at the max health") {
            val player = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 5.0
            }

            player addHealth 5.0
            player.health shouldBe 10.0


            player addHealth 12.0
            player.health shouldBe 20.0
        }

        it("subtracts player health and caps it at 0") {
            val player = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 10.0
            }

            player removeHealth 5.0
            player.health shouldBe 5.0


            player removeHealth 12.0
            player.health shouldBe 0.0
        }

        it("sets the player health restraining it between 0 and the max health") {
            val player = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 10.0
            }

            player setHealth 5.0
            player.health shouldBe 5.0


            player setHealth 22.0
            player.health shouldBe 20.0

            player setHealth -3.0
            player.health shouldBe 0.0
        }
    }
})
