package com.thebluehats.server.game.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LoreParserTest : DescribeSpec({
    describe("lore parser") {
        it("replaces a single variable") {
            val elp = BasicLoreParser("Hello world {0}!")

            val expectedLore = arrayListOf("§7Hello world 1!")

            elp.setVariables(arrayOf("1", "2", "3"))

            elp.parse() shouldBe expectedLore
        }
    }
})
