package com.thebluehats.server.core.services

import com.google.inject.Inject
import com.google.inject.Injector
import com.thebluehats.server.game.enchants.*
import com.thebluehats.server.game.managers.enchants.CustomEnchant
import com.thebluehats.server.game.managers.enchants.GlobalTimer
import com.thebluehats.server.game.utils.Registerer

class CustomEnchantService @Inject constructor(
    private val registerer: Registerer<CustomEnchant>,
    private val globalTimer: GlobalTimer
) : Service {
    override fun provision(injector: Injector) {
        val booboo = injector.getInstance(BooBoo::class.java)
        val doubleJump = injector.getInstance(DoubleJump::class.java)
        registerer.register(
            arrayOf(
                injector.getInstance(Assassin::class.java), injector.getInstance(
                    BeatTheSpammers::class.java
                ),
                injector.getInstance(Billionaire::class.java), booboo, injector.getInstance(
                    BottomlessQuiver::class.java
                ),
                injector.getInstance(Bruiser::class.java), injector.getInstance(BulletTime::class.java),
                injector.getInstance(Chipping::class.java), injector.getInstance(ComboDamage::class.java),
                injector.getInstance(ComboHeal::class.java), injector.getInstance(ComboStun::class.java),
                injector.getInstance(ComboSwift::class.java), injector.getInstance(CriticallyFunky::class.java),
                injector.getInstance(Crush::class.java), injector.getInstance(DevilChicks::class.java),
                injector.getInstance(DiamondAllergy::class.java), doubleJump, injector.getInstance(
                    Executioner::class.java
                ),
                injector.getInstance(Explosive::class.java), injector.getInstance(
                    FancyRaider::class.java
                ),
                injector.getInstance(Fletching::class.java), injector.getInstance(
                    FractionalReserve::class.java
                ),
                injector.getInstance(Gamble::class.java), injector.getInstance(GoldAndBoosted::class.java),
                injector.getInstance(Healer::class.java), injector.getInstance(KingBuster::class.java),
                injector.getInstance(Knockback::class.java), injector.getInstance(LastStand::class.java),
                injector.getInstance(Lifesteal::class.java), injector.getInstance(LuckyShot::class.java),
                injector.getInstance(MegaLongBow::class.java), injector.getInstance(
                    Mirror::class.java
                ),
                injector.getInstance(PainFocus::class.java), injector.getInstance(Parasite::class.java),
                injector.getInstance(Peroxide::class.java), injector.getInstance(Perun::class.java),
                injector.getInstance(Punisher::class.java), injector.getInstance(PushComesToShove::class.java),
                injector.getInstance(RingArmor::class.java), injector.getInstance(Robinhood::class.java),
                injector.getInstance(Shark::class.java), injector.getInstance(Sharp::class.java),
                injector.getInstance(Solitude::class.java), injector.getInstance(SpeedyHit::class.java),
                injector.getInstance(SprintDrain::class.java), injector.getInstance(Telebow::class.java),
                injector.getInstance(Volley::class.java), injector.getInstance(Wasp::class.java),
                injector.getInstance(DiamondStomp::class.java)
            )
        )
        globalTimer.addListener(booboo)
        globalTimer.addListener(doubleJump)
    }
}