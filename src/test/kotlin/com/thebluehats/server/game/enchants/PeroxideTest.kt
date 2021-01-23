package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PeroxideTest : DescribeSpec({
    describe("peroxide") {
        it("should give regen when hit") {
            val peroxide = Peroxide(mockk(), mockk())
            val effect = PotionEffect(PotionEffectType.REGENERATION, 8 * 20, 1)

            val damageeMock = mockk<Player> {
                every { addPotionEffect(effect, true) } returns true
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damagee } returns damageeMock
            }

            peroxide.execute(data)

            verify { damageeMock.addPotionEffect(effect, true) }
        }
    }
})