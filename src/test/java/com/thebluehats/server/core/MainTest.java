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
    @Ignore
    @Test
    public void LogsTitleMessageOnEnable() {
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(PluginCommand.class);

        Main main = mock(Main.class);
        Logger logger = mock(Logger.class);
        CustomEnchantManager customEnchantManager = spy(new CustomEnchantManager(main));

        doCallRealMethod().when(main).onEnable();
        when(main.getCommand(any())).thenReturn(mock(PluginCommand.class));

        doNothing().when(customEnchantManager).registerEnchant(any());
//        doNothing().when(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());

        when(Bukkit.getLogger()).thenReturn(logger);
        doNothing().when(logger).info(anyString());

        main.onEnable();

        verify(logger).info("\n" +
                "\n" +
                "  _______ _            ____  _              _    _       _          _____                          \n" +
                " |__   __| |          |  _ \\| |            | |  | |     | |        / ____|                         \n" +
                "    | |  | |__   ___  | |_) | |_   _  ___  | |__| | __ _| |_ ___  | (___   ___ _ ____   _____ _ __ \n" +
                "    | |  | '_ \\ / _ \\ |  _ <| | | | |/ _ \\ |  __  |/ _` | __/ __|  \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|\n" +
                "    | |  | | | |  __/ | |_) | | |_| |  __/ | |  | | (_| | |_\\__ \\  ____) |  __/ |   \\ V /  __/ |   \n" +
                "    |_|  |_| |_|\\___| |____/|_|\\__,_|\\___| |_|  |_|\\__,_|\\__|___/ |_____/ \\___|_|    \\_/ \\___|_|   \n" +
                "\n" +
                "   ___        ___ _                                             \n" +
                "  | _ )_  _  / __| |_ _____ _____ _ __  _ __  _ __  _ __  _ __  \n" +
                "  | _ \\ || | \\__ \\  _/ -_) V / -_) '  \\| '  \\| '  \\| '  \\| '  \\ \n" +
                "  |___/\\_, | |___/\\__\\___|\\_/\\___|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|\n" +
                "       |__/                                                     \n");

//        verify(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Ignore
    @Test
    public void EnchantsAreRegistered() {
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(PluginCommand.class);

        Main main = mock(Main.class);
        Logger logger = mock(Logger.class);
        CustomEnchantManager customEnchantManager = spy(new CustomEnchantManager(main));

        doCallRealMethod().when(main).onEnable();
        when(main.getCommand(any())).thenReturn(mock(PluginCommand.class));

        doNothing().when(customEnchantManager).registerEnchant(any());
//        doCallRealMethod().when(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());

        when(Bukkit.getLogger()).thenReturn(logger);
        doNothing().when(logger).info(anyString());

        DamageManager damageManager = new DamageManager(customEnchantManager, new CombatManager(main));
        CombatManager combatManager = new CombatManager(main);
        BowManager bowManager = new BowManager();
        GrindingSystem grindingSystem = new GrindingSystem();
        WorldSelectionManager worldSelectionManager = new WorldSelectionManager(main);
        PerkManager perkManager = spy(new PerkManager());

//        ((GameLogicProvider) main).registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem,
//                customEnchantManager, worldSelectionManager, perkManager);

        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
    }

    @Ignore
    @Test
    public void CommandsAreRegistered() {
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(PluginCommand.class);

        Main main = mock(Main.class);
        Logger logger = mock(Logger.class);
        CustomEnchantManager customEnchantManager = spy(new CustomEnchantManager(main));

        doCallRealMethod().when(main).onEnable();
        when(main.getCommand(any())).thenReturn(mock(PluginCommand.class));

        doNothing().when(customEnchantManager).registerEnchant(any());
//        doCallRealMethod().when(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());

        when(Bukkit.getLogger()).thenReturn(logger);
        doNothing().when(logger).info(anyString());

        DamageManager damageManager = new DamageManager(customEnchantManager, new CombatManager(main));
        CombatManager combatManager = new CombatManager(main);
        BowManager bowManager = new BowManager();
        GrindingSystem grindingSystem = new GrindingSystem();
        WorldSelectionManager worldSelectionManager = new WorldSelectionManager(main);
        PerkManager perkManager = spy(new PerkManager());

//        ((GameLogicProvider) main).registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem,
//                customEnchantManager, worldSelectionManager, perkManager);

        verify(main, atLeast(1)).getCommand(any());
    }

    @Ignore
    @Test
    public void PerksAreRegistered() {
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.mockStatic(PluginCommand.class);

        Main main = mock(Main.class);
        Logger logger = mock(Logger.class);
        CustomEnchantManager customEnchantManager = spy(new CustomEnchantManager(main));

        doCallRealMethod().when(main).onEnable();
        when(main.getCommand(any())).thenReturn(mock(PluginCommand.class));

        doNothing().when(customEnchantManager).registerEnchant(any());
//        doCallRealMethod().when(main).registerGameLogic(any(), any(), any(), any(), any(), any(), any(), any());

        when(Bukkit.getLogger()).thenReturn(logger);
        doNothing().when(logger).info(anyString());

        DamageManager damageManager = new DamageManager(customEnchantManager, new CombatManager(main));
        CombatManager combatManager = new CombatManager(main);
        BowManager bowManager = new BowManager();
        GrindingSystem grindingSystem = new GrindingSystem();
        WorldSelectionManager worldSelectionManager = new WorldSelectionManager(main);
        PerkManager perkManager = spy(new PerkManager());

//        ((GameLogicProvider) main).registerGameLogic(main, damageManager, combatManager, bowManager, grindingSystem,
//                customEnchantManager, worldSelectionManager, perkManager);

        verify(perkManager, atLeast(1)).registerPerk(any());
    }
}
