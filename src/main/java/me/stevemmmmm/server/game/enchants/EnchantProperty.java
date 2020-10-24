package me.stevemmmmm.server.game.enchants;

public class EnchantProperty<T> {
    private final T[] values;

    @SafeVarargs
    public EnchantProperty(T... values) {
        this.values = values;
    }

    public T getValueAtLevel(int level) {
        return values[level - 1];
    }
}
