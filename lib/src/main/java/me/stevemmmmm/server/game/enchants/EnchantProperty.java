package me.stevemmmmm.server.game.enchants;

public class EnchantProperty<T> {
    private T[] values;

    @SafeVarargs
    public EnchantProperty(T... values) {
        if (values.length > 3)
            throw new IllegalArgumentException("Enchants only have 3 levels. You had " + values.length + "properties.");

        this.values = values;
    }

    public T getValueAtLevel(int level) {
        return values[level - 1];
    }
}
