package me.stevemmmmm.server.core;

import me.stevemmmmm.server.game.enchants.CustomEnchantManager;
import me.stevemmmmm.server.game.managers.*;
import me.stevemmmmm.server.game.perks.PerkManager;

public interface Registerer {
    void registerGameLogic(Main main, DamageManager damageManager, CombatManager combatManager,
                           BowManager bowManager, GrindingSystem grindingSystem,
                           CustomEnchantManager customEnchantManager,
                           WorldSelectionManager worldSelectionManager, PerkManager perkManager);
}
