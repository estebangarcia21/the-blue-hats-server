package com.thebluehats.server.game.enchants

import com.thebluehats.server.game.managers.enchants.Timer
import com.thebluehats.server.game.managers.enchants.processedevents.DamageEventEnchantData
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class AssassinTest : DescribeSpec({
    describe("assassin") {
        it("teleports the player behind the player who damages them") {
            val timerMock = mockk<Timer<Player>> {
                every { start(any(), any(), any()) } returns Unit
                every { isRunning(any()) } returns false
            }

            val assassin = Assassin(timerMock, mockk(), mockk())

            val mockBlock = mockk<Block> {
                every { type } returns Material.AIR
            }

            val worldMock = mockk<World> {
                every { getBlockAt(any()) } returns mockBlock
                every { playSound(any(), any(), any(), any()) } returns Unit
            }

            val damageeMock = mockk<Player> {
                every { location } returns Location(worldMock, 0.0, 0.0, 0.0)
                every { world } returns worldMock
                every { teleport(any<Location>()) } returns true
                every { teleport(any<Entity>()) } returns true
            }

            val damagerMockLocation = Location(worldMock, 0.0, 0.0, 0.0)
            val damagerEyeLocation = Location(worldMock, 0.0, 1.0, 0.0)

            val damagerMock = mockk<Player> {
                every { location } returns damagerMockLocation
                every { eyeLocation } returns damagerEyeLocation
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damagee } returns damageeMock
                every { damager } returns damagerMock
            }

            assassin.execute(data)
            
            verify { damageeMock.teleport(damagerMockLocation.subtract(damagerEyeLocation)) }
        }

        it("teleports the player to the player who damages them if there is no room behind them") {
            val timerMock = mockk<Timer<Player>> {
                every { start(any(), any(), any()) } returns Unit
                every { isRunning(any()) } returns false
            }

            val assassin = Assassin(timerMock, mockk(), mockk())

            val mockBlock = mockk<Block> {
                every { type } returns Material.AIR
            }

            val worldMock = mockk<World> {
                every { getBlockAt(any()) } returns mockBlock
                every { playSound(any(), any(), any(), any()) } returns Unit
            }

            val damageeMock = mockk<Player> {
                every { location } returns Location(worldMock, 0.0, 0.0, 0.0)
                every { world } returns worldMock
                every { teleport(any<Location>()) } returns true
                every { teleport(any<Entity>()) } returns true
            }

            val damagerMockLocation = Location(worldMock, 0.0, 0.0, 0.0)
            val damagerEyeLocation = Location(worldMock, 0.0, 1.0, 0.0)

            val damagerMock = mockk<Player> {
                every { location } returns damagerMockLocation
                every { eyeLocation } returns damagerEyeLocation
            }

            val data = mockk<DamageEventEnchantData> {
                every { level } returns 3
                every { damagee } returns damageeMock
                every { damager } returns damagerMock
            }

            assassin.execute(data)

            verify { damageeMock.teleport(damagerMockLocation) }
        }
    }
})
