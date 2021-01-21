package com.thebluehats.server.game.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LoreParserTest : DescribeSpec({
    describe("lore parser") {
        it("replaces variable templates") {
            val loreParser = LoreParser("I said {0} {1}!")

            loreParser.setVariables(arrayOf("hello", "world"))

            val output = loreParser.parse()

            output shouldBe "I said hello world!"
        }
    }
})
