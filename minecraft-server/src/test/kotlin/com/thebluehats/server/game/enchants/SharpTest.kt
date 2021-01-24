package com.thebluehats.server.game.enchants
import com.thebluehats.server.game.managers.combat.CalculationMode
import com.thebluehats.server.game.managers.combat.DamageManager
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.event.entity.EntityDamageByEntityEvent

class SharpTest : DescribeSpec({
    describe("sharp") {
        it("should do a certain amount more damage"){
            val eventMock = mockk<EntityDamageByEntityEvent>()

            val damageManagerMock = mockk<DamageManager> {
                every { addDamage(eventMock, .12, CalculationMode.ADDITIVE) } returns Unit
            }

            val sharp = Sharp(damageManagerMock, mockk())

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { event }  returns eventMock
            }

            sharp.execute(data)

            verify { damageManagerMock.addDamage(eventMock, .12, CalculationMode.ADDITIVE) }
        }
    }
})