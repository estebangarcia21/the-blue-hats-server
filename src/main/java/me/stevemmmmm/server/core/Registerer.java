package me.stevemmmmm.server.core;

import me.stevemmmmm.server.game.enchants.CustomEnchantManager;
import me.stevemmmmm.server.game.managers.BowManager;
import me.stevemmmmm.server.game.managers.DamageManager;

public interface Registerer {
    public void registerEnchants(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager);

    public void registerPerks(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager);

    public void registerCommands(Main main, CustomEnchantManager customEnchantManager);
}
