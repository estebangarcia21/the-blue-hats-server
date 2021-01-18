package com.thebluehats.server.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thebluehats.server.game.enchants.*;
import com.thebluehats.server.game.managers.enchants.CustomEnchant;
import com.thebluehats.server.game.managers.enchants.GlobalTimer;
import com.thebluehats.server.game.utils.Registerer;

public class CustomEnchantService implements Service {
    private final Registerer<CustomEnchant> registerer;
    private final GlobalTimer globalTimer;

    @Inject
    public CustomEnchantService(Registerer<CustomEnchant> registerer, GlobalTimer globalTimer) {
        this.registerer = registerer;
        this.globalTimer = globalTimer;
    }

    @Override
    public void provision(Injector injector) {
        BooBoo booboo = injector.getInstance(BooBoo.class);
        DoubleJump doubleJump = injector.getInstance(DoubleJump.class);
      
        registerer.register(new CustomEnchant[] { injector.getInstance(BeatTheSpammers.class),
                injector.getInstance(Wasp.class), injector.getInstance(Peroxide.class),
                injector.getInstance(SprintDrain.class), injector.getInstance(ComboSwift.class),
                injector.getInstance(ComboDamage.class), injector.getInstance(Healer.class),
                injector.getInstance(LastStand.class), injector.getInstance(Mirror.class),
                injector.getInstance(Billionaire.class), injector.getInstance(DiamondStomp.class),
                injector.getInstance(Punisher.class), injector.getInstance(DiamondAllergy.class),
                injector.getInstance(Solitude.class), injector.getInstance(Assassin.class),
                injector.getInstance(Fletching.class), injector.getInstance(KingBuster.class),
                injector.getInstance(Knockback.class), injector.getInstance(Parasite.class),
                injector.getInstance(FractionalReserve.class), injector.getInstance(PainFocus.class),
                injector.getInstance(CriticallyFunky.class), injector.getInstance(Bruiser.class),
                injector.getInstance(BulletTime.class), booboo, injector.getInstance(ComboHeal.class),
                injector.getInstance(ComboStun.class), injector.getInstance(DevilChicks.class),
                injector.getInstance(Perun.class), injector.getInstance(Crush.class),
                injector.getInstance(RingArmor.class), injector.getInstance(FancyRaider.class),
                injector.getInstance(PushComesToShove.class), injector.getInstance(Volley.class),
                injector.getInstance(Explosive.class), injector.getInstance(Telebow.class),
                injector.getInstance(Robinhood.class), injector.getInstance(MegaLongBow.class),
                injector.getInstance(LuckyShot.class), doubleJump, injector.getInstance(BottomlessQuiver.class),
                injector.getInstance(LuckyShot.class), doubleJump, injector.getInstance(Gamble.class), 
                injector.getInstance(Chipping.class) });

        globalTimer.addListener(booboo);
        globalTimer.addListener(doubleJump);
    }
}
