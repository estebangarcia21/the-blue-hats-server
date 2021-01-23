package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class SpeedyHitTest : DescribeSpec( {
    describe("speedy hit") {
        it("gives speed on hit") {
            val uuid = UUID.randomUUID()

            val timerMock: Timer<UUID> = mockk {
                every { isRunning(uuid) } returns false
                every { start(uuid, 1, seconds = true) } returns Unit
            }

            val speedyHit = SpeedyHit(timerMock, mockk())
            val effect = PotionEffect(PotionEffectType.SPEED, 9 * 20, 0)

            val damagerMock: Player = mockk {
                every { addPotionEffect(effect, true) } returns true
                every { uniqueId } returns uuid
            }

            val data: DamageEventEnchantData = mockk {
                every { level } returns 3
                every { damager } returns damagerMock
            }

            speedyHit.execute(data)

            verify { damagerMock.addPotionEffect(effect, true) }
        }
    }
})
