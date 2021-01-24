package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class WaspTest : DescribeSpec({
    describe("wasp") {
        it("gives weakness to the hit player") {
            val wasp = Wasp(mockk())
            val effect = PotionEffect(PotionEffectType.WEAKNESS, 16 * 20, 3)

            val damageeMock = mockk<Player> {
                every { addPotionEffect(effect, true) } returns true
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damagee } returns damageeMock
            }

            wasp.execute(data)

            verify { damageeMock.addPotionEffect(effect, true) }
        }
    }
})
