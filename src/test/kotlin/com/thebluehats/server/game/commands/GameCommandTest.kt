package com.thebluehats.server.game.commands

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.bukkit.entity.Player

class GameCommandTest : DescribeSpec({
    describe("game command") {
        it("executes a command based on an alias") {
            val gc: GameCommand = spyk(object : GameCommand() {
                override val commandNames: Array<String> get() = arrayOf("a", "b", "c", "d")

                override fun getUsageMessage(cmd: String?): String? = null

                override fun runCommand(player: Player, cmd: String?, args: Array<String>) {}
            })

            gc.onCommand(mockk<Player>(), mockk(), "b", emptyArray())

            verify { gc.runCommand(any(), any(), any()) }
        }
    }
})
