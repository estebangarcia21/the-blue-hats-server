package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.HitCounter
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class ComboSwiftTest : DescribeSpec ({
    describe("combo swift") {
        it("should give speed to the damager") {
            val effect = PotionEffect(PotionEffectType.SPEED, 5 * 20, 1)

            val damagerMock = mockk<Player> {
                every { addPotionEffect(effect,true) } returns true
            }

            val hitCounterMock = mockk<HitCounter> {
                every { addOne(damagerMock) } returns Unit
                every { hasHits(damagerMock, 3) } returns true
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damager} returns damagerMock
            }

            val comboSwift = ComboSwift(hitCounterMock, mockk())

            comboSwift.execute(data)

            verify { damagerMock.addPotionEffect(effect, true) }
        }
    }
})