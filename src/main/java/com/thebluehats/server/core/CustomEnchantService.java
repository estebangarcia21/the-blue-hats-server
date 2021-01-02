package com.thebluehats.server.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thebluehats.server.game.enchants.*;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.utils.Registerer;

public class CustomEnchantService implements Service {
    private final Registerer<CustomEnchant> registerer;

    @Inject
    public CustomEnchantService(Registerer<CustomEnchant> registerer) {
        this.registerer = registerer;
    }

    @Override
    public void provision(Injector injector) {
        registerer.register(new CustomEnchant[] { injector.getInstance(BeatTheSpammers.class), injector.getInstance(Wasp.class),
                injector.getInstance(Peroxide.class), injector.getInstance(SprintDrain.class),
                injector.getInstance(ComboSwift.class), injector.getInstance(ComboDamage.class),
                injector.getInstance(Healer.class), injector.getInstance(LastStand.class),
                injector.getInstance(Mirror.class), injector.getInstance(Billionaire.class),
                injector.getInstance(DiamondStomp.class), injector.getInstance(Punisher.class),
                injector.getInstance(DiamondAllergy.class), injector.getInstance(Solitude.class),
                injector.getInstance(Assassin.class), injector.getInstance(Fletching.class),
                injector.getInstance(KingBuster.class), injector.getInstance(Knockback.class),
                injector.getInstance(Parasite.class), injector.getInstance(FractionalReserve.class),
                injector.getInstance(PainFocus.class), injector.getInstance(CriticallyFunky.class),
                injector.getInstance(BooBoo.class)
        });
    }
}
