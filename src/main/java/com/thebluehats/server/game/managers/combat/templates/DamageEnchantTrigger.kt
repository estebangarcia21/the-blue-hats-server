package com.thebluehats.server.game.managers.combat.templates

import com.google.common.collect.ImmutableMap
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.CustomEnchantUtils
import com.thebluehats.server.game.managers.enchants.DamageTriggeredEnchant
import com.thebluehats.server.game.utils.EntityValidator
import org.bukkit.Material
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import java.util.function.Function

abstract class DamageEnchantTrigger protected constructor(protected val customEnchantUtils: CustomEnchantUtils) {
    private enum class MaterialMatcher {
        LEGGINGS, SWORD, BOW
    }

    protected fun getItemMap(enchant: DamageTriggeredEnchant, inventory: PlayerInventory): ImmutableMap<Material, Int> {
        val mapBuilder = ImmutableMap.builder<Material, Int>()
        for (material in enchant.enchantItemTypes) {
            val materialName = material.toString()
            val materialMatchers = MaterialMatcher.values()
            for (materialMatcher in materialMatchers) {
                if (materialName.contains(materialMatcher.toString())) {
                    val getItem = materialFunctions[materialMatcher]!!
                    mapBuilder.put(material, customEnchantUtils.getEnchantLevel(enchant, getItem.apply(inventory)))
                }
            }
        }
        return mapBuilder.build()
    }

    protected fun inventoryHasEnchant(inventory: PlayerInventory, enchant: CustomEnchant): Boolean {
        return customEnchantUtils.itemHasEnchant(enchant, getItemFunction(enchant)!!.apply(inventory))
    }

    private fun getItemFunction(enchant: CustomEnchant): Function<PlayerInventory, ItemStack>? {
        for (material in enchant.enchantItemTypes) {
            val materialName = material.toString()
            val materialMatchers = MaterialMatcher.values()
            for (materialMatcher in materialMatchers) {
                if (materialName.contains(materialMatcher.toString())) {
                    return materialFunctions[materialMatcher]
                }
            }
        }
        return null
    }

    abstract fun run(
        enchant: DamageTriggeredEnchant, event: EntityDamageByEntityEvent, targetPlayer: EnchantHolder, validators: Array<EntityValidator>
    )

    companion object {
        private val materialFunctions = ImmutableMap
            .builder<MaterialMatcher, Function<PlayerInventory, ItemStack>>()
            .put(MaterialMatcher.LEGGINGS, Function { obj: PlayerInventory -> obj.leggings })
            .put(MaterialMatcher.SWORD, Function { obj: PlayerInventory -> obj.itemInHand })
            .put(MaterialMatcher.BOW, Function { obj: PlayerInventory -> obj.itemInHand }).build()
    }
}