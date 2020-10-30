package me.stevemmmmm.server.core;

import me.stevemmmmm.server.game.enchants.CustomEnchantManager;
import me.stevemmmmm.server.game.managers.*;

public interface Registerer {
    void registerEnchants(DamageManager damageManager, BowManager bowManager, CustomEnchantManager customEnchantManager);

    void registerPerks(DamageManager damageManager, BowManager bowManager, CustomEnchantManager customEnchantManager);

    void registerCommands(Main main, DamageManager damageManager, CombatManager combatManager, GrindingSystem grindingSystem, CustomEnchantManager customEnchantManager, WorldSelectionManager worldSelectionManager);
}
