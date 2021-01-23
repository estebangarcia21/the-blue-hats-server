package com.thebluehats.server.game.managers.enchants

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CustomEnchantUtilsTest : DescribeSpec({
    describe("custom enchant utils") {
        it("checks if a type is an acceptable enchant type") {
            val utils = CustomEnchantUtils()

            val enchant = mockk<CustomEnchant> {
                every { name } returns "Abc"
                every { isRareEnchant } returns false
                every { enchantItemTypes } returns arrayOf(Material.BOW, Material.GOLD_SWORD)
            }

            utils.isCompatibleWith(enchant, Material.LEATHER_LEGGINGS) shouldBe false
            utils.isCompatibleWith(enchant, Material.BOW) shouldBe true
        }

        it("checks if an enchant exists on an item") {
            val utils = CustomEnchantUtils()

            val enchant = mockk<CustomEnchant> {
                every { name } returns "Abc"
                every { isRareEnchant } returns false
            }

            val nonExistentEnchantLore = arrayListOf("Def")
            val existentEnchantLore = arrayListOf("Abc")

            val itemMock = mockk<ItemStack> {
                every { type } returns Material.BOW
                every { itemMeta } returns mockk {
                    every { lore } returnsMany listOf(nonExistentEnchantLore, existentEnchantLore)
                }
            }

            utils.itemHasEnchant(enchant, itemMock) shouldBe false
            utils.itemHasEnchant(enchant, itemMock) shouldBe true
        }

        it("gets the level of an enchant from an item") {
            val utils = CustomEnchantUtils()

            val enchant = mockk<CustomEnchant> {
                every { name } returns "Abc"
                every { isRareEnchant } returns false
            }

            val enchantLoreL1 = arrayListOf("Abc")
            val enchantLoreL2 = arrayListOf("Abc II")
            val enchantLoreL3 = arrayListOf("Abc III")

            val itemMock = mockk<ItemStack> {
                every { type } returns Material.BOW
                every { itemMeta } returns mockk {
                    every { lore } returnsMany listOf(enchantLoreL1, enchantLoreL2, enchantLoreL3)
                }
            }

            utils.getEnchantLevel(enchant, itemMock) shouldBe 1
            utils.getEnchantLevel(enchant, itemMock) shouldBe 2
            utils.getEnchantLevel(enchant, itemMock) shouldBe 3
        }
    }
})
