package com.thebluehats.server.game.utils

import com.thebluehats.server.game.utils.PlayerHealth.Utils.addHealth
import com.thebluehats.server.game.utils.PlayerHealth.Utils.removeHealth
import com.thebluehats.server.game.utils.PlayerHealth.Utils.setHealth
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player

class PlayerHealthTest : DescribeSpec({
    describe("player health utils") {
        it("adds player health and caps it at the max health") {
            val player = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 5.0
                every { health = any() } returns Unit
            }

            player addHealth 5.0
            verify { player.health = 10.0 }

            player addHealth 24.0
            verify { player.health = 20.0 }
        }

        it("subtracts player health and caps it at 0") {
            val player = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 5.0
                every { health = any() } returns Unit
            }

            player removeHealth 2.0
            verify { player.health = 3.0 }

            player removeHealth 10.0
            verify { player.health = 0.0 }
        }

        it("sets the player health restraining it between 0 and the max health") {
            val player = mockk<Player> {
                every { maxHealth } returns 20.0
                every { health } returns 10.0
                every { health = any() } returns Unit
            }

            player setHealth 5.0
            verify { player.health = 5.0 }

            player setHealth 22.0
            verify { player.health = 20.0 }

            player setHealth -3.0
            verify { player.health = 0.0 }
        }
    }
})
