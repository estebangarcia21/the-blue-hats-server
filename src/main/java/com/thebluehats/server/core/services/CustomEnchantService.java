package com.thebluehats.server.core.services;

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

        registerer.register(
                new CustomEnchant[] { injector.getInstance(Assassin.class), injector.getInstance(BeatTheSpammers.class),
                        injector.getInstance(Billionaire.class), booboo, injector.getInstance(BottomlessQuiver.class),
                        injector.getInstance(Bruiser.class), injector.getInstance(BulletTime.class),
                        injector.getInstance(Chipping.class), injector.getInstance(ComboDamage.class),
                        injector.getInstance(ComboHeal.class), injector.getInstance(ComboStun.class),
                        injector.getInstance(ComboSwift.class), injector.getInstance(CriticallyFunky.class),
                        injector.getInstance(Crush.class), injector.getInstance(DevilChicks.class),
                        injector.getInstance(DiamondAllergy.class), doubleJump, injector.getInstance(Executioner.class),
                        injector.getInstance(Explosive.class), injector.getInstance(FancyRaider.class),
                        injector.getInstance(Fletching.class), injector.getInstance(FractionalReserve.class),
                        injector.getInstance(Gamble.class), injector.getInstance(GoldAndBoosted.class),
                        injector.getInstance(Healer.class), injector.getInstance(KingBuster.class),
                        injector.getInstance(Knockback.class), injector.getInstance(LastStand.class),
                        injector.getInstance(Lifesteal.class), injector.getInstance(LuckyShot.class),
                        injector.getInstance(MegaLongBow.class), injector.getInstance(Mirror.class),
                        injector.getInstance(PainFocus.class), injector.getInstance(Parasite.class),
                        injector.getInstance(Peroxide.class), injector.getInstance(Perun.class),
                        injector.getInstance(Punisher.class), injector.getInstance(PushComesToShove.class),
                        injector.getInstance(RingArmor.class), injector.getInstance(Robinhood.class),
                        injector.getInstance(Shark.class), injector.getInstance(Sharp.class),
                        injector.getInstance(Solitude.class), injector.getInstance(SpeedyHit.class),
                        injector.getInstance(SprintDrain.class), injector.getInstance(Telebow.class),
                        injector.getInstance(Volley.class), injector.getInstance(Wasp.class) });

        globalTimer.addListener(booboo);
        globalTimer.addListener(doubleJump);
    }
}
