package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kong.unirest.Unirest.isRunning
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class CrushTest : DescribeSpec({
    describe("crush") {
        it("gives weakness to the damagee") {
            val uuid = UUID.randomUUID()

            val timerMock = mockk<Timer<UUID>> {
                every { isRunning(uuid) } returns false
                every { start(uuid, 40) } returns Unit
            }

            val crush = Crush(timerMock, mockk())
            val effect = PotionEffect(PotionEffectType.WEAKNESS, 10, 6)

            val damageeMock = mockk<Player> {
                every { addPotionEffect(effect, true) } returns true
            }

            val damagerMock = mockk<Player> {
                every { uniqueId } returns uuid
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damagee } returns damageeMock
                every { damager } returns damagerMock
            }

            crush.execute(data)

            verify { damageeMock.addPotionEffect(effect, true) }
        }
    }
})
