package com.thebluehats.server.core;

import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.game.GrindingSystem;
import com.thebluehats.server.game.managers.game.PerkManager;
import com.thebluehats.server.game.managers.game.WorldSelectionManager;

public interface Registerer {
    void registerGameLogic(Main main, DamageManager damageManager, CombatManager combatManager,
                           BowManager bowManager, GrindingSystem grindingSystem,
                           CustomEnchantManager customEnchantManager,
                           WorldSelectionManager worldSelectionManager, PerkManager perkManager);
}
