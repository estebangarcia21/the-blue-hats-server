package com.thebluehats.server.game.utils

import com.thebluehats.server.game.managers.enchants.CustomEnchant
import java.util.*

class SortCustomEnchantByName : Comparator<CustomEnchant> {
    override fun compare(a: CustomEnchant, b: CustomEnchant): Int {
        try {
            return a.name.compareTo(b.name)
        } catch (exception: NullPointerException) {
            println("Error! Missing name information for an enchant.")
        }
        return 0
    }
}