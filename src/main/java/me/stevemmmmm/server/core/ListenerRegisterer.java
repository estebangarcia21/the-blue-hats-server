package me.stevemmmmm.server.core;

import me.stevemmmmm.server.game.enchants.CustomEnchantManager;
import me.stevemmmmm.server.game.enchants.Wasp;
import me.stevemmmmm.server.game.managers.BowManager;
import me.stevemmmmm.server.game.managers.DamageManager;

public class ListenerRegisterer {
    public void registerObjects(Main mainInstance) {
        DamageManager damageManager = new DamageManager();
        BowManager bowManager = new BowManager();
        CustomEnchantManager customEnchantManager = new CustomEnchantManager(mainInstance);

        registerEnchants(damageManager, bowManager, customEnchantManager);
        registerPerks(damageManager, bowManager, customEnchantManager);
    }

    private void registerEnchants(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager) {
        customEnchantManager.registerEnchant(new Wasp(bowManager));
    }

    private void registerPerks(DamageManager damageManager, BowManager bowManager,
            CustomEnchantManager customEnchantManager) {
    }
}
