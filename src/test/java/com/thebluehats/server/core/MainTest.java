package com.thebluehats.server.core;

import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.enchants.CustomEnchantManager;
import com.thebluehats.server.game.managers.game.GrindingSystem;
import com.thebluehats.server.game.managers.game.PerkManager;
import com.thebluehats.server.game.managers.combat.BowManager;
import com.thebluehats.server.game.managers.combat.DamageManager;
import com.thebluehats.server.game.managers.game.WorldSelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Bukkit.class, PluginCommand.class })
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
    private PerkManager perkManager;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(PluginCommand.class);

        registerer = main;

        damageManager = new DamageManager(customEnchantManager, new CombatManager(mock(Main.class)));
        combatManager = new CombatManager(main);
        bowManager = new BowManager();
        grindingSystem = new GrindingSystem();
        worldSelectionManager = new WorldSelectionManager(main);
        customEnchantManager = spy(new CustomEnchantManager(main));
        perkManager = spy(new PerkManager());

        when(main.getCommand(any())).thenReturn(mock(PluginCommand.class));
        when(Bukkit.getLogger()).thenReturn(logger);

        doCallRealMethod().when(main).onEnable();
        doCallRealMethod().when(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());

        doNothing().when(logger).info(anyString());
        doNothing().when(customEnchantManager).registerEnchant(any());
    }

    @Test
    public void LogsTitleMessageOnEnable() {
        doNothing().when(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());

        main.onEnable();

        verify(logger).info("   The Hypixel Pit Remake by Stevemmmmm   ");
        verify(logger, times(2)).info("------------------------------------------");

        verify(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void EnchantsAreRegistered() {
        registerer.registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem,
                customEnchantManager, worldSelectionManager, perkManager);

        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
    }

    @Test
    public void CommandsAreRegistered() {
        registerer.registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem,
                customEnchantManager, worldSelectionManager, perkManager);

        verify(main, atLeast(1)).getCommand(any());
    }

    @Test
    public void PerksAreRegistered() {
        registerer.registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem,
                customEnchantManager, worldSelectionManager, perkManager);

        verify(perkManager, atLeast(1)).registerPerk(any());
    }
}
