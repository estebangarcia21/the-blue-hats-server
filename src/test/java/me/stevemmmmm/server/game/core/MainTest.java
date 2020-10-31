package me.stevemmmmm.server.game.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.logging.Logger;

import me.stevemmmmm.server.game.managers.*;
import org.bukkit.Bukkit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import me.stevemmmmm.server.core.Main;
import me.stevemmmmm.server.core.Registerer;
import me.stevemmmmm.server.game.enchants.CustomEnchantManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Bukkit.class })
public class MainTest {
    @Mock
    private Main main;
    @Mock
    private Logger logger;

    private Registerer registerer;

    private DamageManager damageManager;
    private CombatManager combatManager;
    private BowManager bowManager;
    private GrindingSystem grindingSystem;
    private WorldSelectionManager worldSelectionManager;
    private CustomEnchantManager customEnchantManager;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(Bukkit.class);

        registerer = main;

        damageManager = new DamageManager(customEnchantManager, new CombatManager(mock(Main.class)));
        combatManager = new CombatManager(main);
        bowManager = new BowManager();
        grindingSystem = new GrindingSystem();
        worldSelectionManager = new WorldSelectionManager(main);
        customEnchantManager = spy(new CustomEnchantManager(main));

        doCallRealMethod().when(main).onEnable();
        doNothing().when(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any());

        logger = mock(Logger.class);

        when(Bukkit.getLogger()).thenReturn(logger);
        doNothing().when(logger).info(anyString());
    }

    @Test
    public void LogsTitleMessageOnEnable() {
        main.onEnable();

        verify(logger).info("   The Hypixel Pit Remake by Stevemmmmm   ");
        verify(logger, times(2)).info("------------------------------------------");

        verify(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void EnchantsAreRegistered() {
        main.onEnable();

        doNothing().when(customEnchantManager).registerEnchant(any());

        registerer.registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem,
                customEnchantManager, worldSelectionManager);

        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
    }
//
//    @Test
//    public void PerksAreRegistered() {
//        main.onEnable();
//
//        doCallRealMethod().when(main).registerEnchants(any(), any(), any());
//        doNothing().when(customEnchantManager).registerEnchant(any());
//
//        registerer.registerGameLogic(main, damageManager, new CombatManager(main), new BowManager(),
//                new GrindingSystem(), customEnchantManager, new WorldSelectionManager(main));
//
//        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
//    }
//
//    @Test
//    public void CommandsAreRegistered() {
//        main.onEnable();
//
//        doCallRealMethod().when(main).registerEnchants(any(), any(), any());
//        doNothing().when(customEnchantManager).registerEnchant(any());
//
//        registerer.registerEnchants(damageManager, new BowManager(), customEnchantManager);
//
//        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
//    }
}
