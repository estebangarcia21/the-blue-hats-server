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
import org.junit.Ignore;
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

        verify(logger).info("\n" +
                "\n" +
                "  _______ _            ____  _              _    _       _          _____                          \n" +
                " |__   __| |          |  _ \\| |            | |  | |     | |        / ____|                         \n" +
                "    | |  | |__   ___  | |_) | |_   _  ___  | |__| | __ _| |_ ___  | (___   ___ _ ____   _____ _ __ \n" +
                "    | |  | '_ \\ / _ \\ |  _ <| | | | |/ _ \\ |  __  |/ _` | __/ __|  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|\n" +
                "    | |  | | | |  __/ | |_) | | |_| |  __/ | |  | | (_| | |_\\__ \\  ____) |  __/ |   \\ V /  __/ |   \n" +
                "    |_|  |_| |_|\\___| |____/|_|\\__,_|\\___| |_|  |_|\\__,_|\\__|___/ |_____/ \\___|_|    \\_/ \\___|_|   \n" +
                "\n");

        verify(logger).info("\n" +
                "\n" +
                "  ___        ___ _                                             \n" +
                " | _ )_  _  / __| |_ _____ _____ _ __  _ __  _ __  _ __  _ __  \n" +
                " | _ \\ || | \\__ \\  _/ -_) V / -_) '  \\| '  \\| '  \\| '  \\| '  \\ \n" +
                " |___/\\_, | |___/\\__\\___|\\_/\\___|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|\n" +
                "      |__/                                                     \n" +
                "\n");

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
