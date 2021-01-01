package com.thebluehats.server.core;

import com.google.inject.Injector;
import com.thebluehats.server.game.managers.world.PerkManager;
import com.thebluehats.server.game.perks.Perk;
import com.thebluehats.server.game.perks.Vampire;
import com.thebluehats.server.game.utils.Registerer;

public class PerksService implements Service {
    @Override
    public void run(Injector injector) {
        Registerer<Perk> registerer = injector.getInstance(PerkManager.class);

        registerer.register(injector.getInstance(Vampire.class));
    }
}
