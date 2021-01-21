package com.thebluehats.server.game.managers.enchants

class EnchantProperty<T> @SafeVarargs constructor(vararg values: T) {
    private val values: Array<out T>

    fun getValueAtLevel(level: Int): T {
        return values[level - 1]
    }

    init {
        require(values.size <= 3) { "Enchants only have 3 levels. You had " + values.size + "properties." }

        this.values = values
    }
}