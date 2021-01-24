package com.thebluehats.server.game.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class EnchantLoreParserTest : DescribeSpec({
    describe("enchant lore parser") {
        it("replaces tags with the proper chat color and linebreak") {
            val elp = EnchantLoreParser("<red>red</red><br/><light-purple>purple</light-purple>")

            val expectedLore = arrayListOf(
                "§7§cred§7",
                "§7§dpurple§7"
            )

            elp.parseForLevel(1) shouldBe expectedLore
        }

        it("replaces a single variable") {
            val elp = EnchantLoreParser("Hello world {0}!")

            val expectedL1Lore = arrayListOf("§7Hello world 1!")
            val expectedL2Lore = arrayListOf("§7Hello world 2!")
            val expectedL3Lore = arrayListOf("§7Hello world 3!")

            elp.setSingleVariable("1", "2", "3")

            elp.parseForLevel(1) shouldBe expectedL1Lore
            elp.parseForLevel(2) shouldBe expectedL2Lore
            elp.parseForLevel(3) shouldBe expectedL3Lore
        }

        it("replaces multiple variables") {
            val elp = EnchantLoreParser("Hello world {0} {1}!")

            val vars: Array<Array<String>?> = arrayOf(
                arrayOf("a", "b", "c"),
                arrayOf("x", "y", "z")
            )

            val expectedL1Lore = arrayListOf("§7Hello world a x!")
            val expectedL2Lore = arrayListOf("§7Hello world b y!")
            val expectedL3Lore = arrayListOf("§7Hello world c z!")

            elp.setVariables(vars)

            elp.parseForLevel(1) shouldBe expectedL1Lore
            elp.parseForLevel(2) shouldBe expectedL2Lore
            elp.parseForLevel(3) shouldBe expectedL3Lore
        }
    }
})
