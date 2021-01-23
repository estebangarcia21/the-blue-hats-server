package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SprintDrainTest : DescribeSpec({
    describe("sprint drain") {
        it ("gives the damager speed one") {
            val sprintDrain = SprintDrain(mockk())

            val effect = PotionEffect(PotionEffectType.SPEED, 5 * 20, 0)

            val damagerMock = mockk<Player> {
                every { addPotionEffect(effect, true) } returns true
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 1
                every { damager } returns damagerMock
            }

            sprintDrain.execute(data)

            verify { damagerMock.addPotionEffect(effect, true) }
        }

        describe("level 2 or greater") {
            it("gives the damager speed and gives the damagee slowness") {

            }
        }
    }
})
