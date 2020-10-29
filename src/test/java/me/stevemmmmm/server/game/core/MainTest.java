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
import me.stevemmmmm.server.game.managers.BowManager;
import me.stevemmmmm.server.game.managers.DamageManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Bukkit.class })
public class MainTest {
    @Mock
    private Main main;

    private CustomEnchantManager customEnchantManager;
    private Registerer registerer;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        customEnchantManager = spy(new CustomEnchantManager(main));
        registerer = main;
    }

    @Test
    public void testOnEnable() {
        doCallRealMethod().when(main).onEnable();
        doNothing().when(main).registerEnchants(any(), any(), any());

        PowerMockito.mockStatic(Bukkit.class);

        Logger logger = mock(Logger.class);

        when(Bukkit.getLogger()).thenReturn(logger);
        doNothing().when(logger).info(anyString());

        main.onEnable();

        verify(logger).info("   The Hypixel Pit Remake by Stevemmmmm   ");
        verify(logger, times(2)).info("------------------------------------------");

        verify(main).registerEnchants(any(), any(), any());
    }

    @Test
    public void testRegisterEnchants() {
        doCallRealMethod().when(main).registerEnchants(any(), any(), any());
        doNothing().when(customEnchantManager).registerEnchant(any());

        registerer.registerEnchants(new DamageManager(), new BowManager(), customEnchantManager);

        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
    }

    @Test
    public void testRegisterPerks() {
        doCallRealMethod().when(main).registerEnchants(any(), any(), any());
        doNothing().when(customEnchantManager).registerEnchant(any());

        registerer.registerEnchants(new DamageManager(), new BowManager(), customEnchantManager);

        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
    }

    @Test
    public void testRegisterCommands() {
        doCallRealMethod().when(main).registerEnchants(any(), any(), any());
        doNothing().when(customEnchantManager).registerEnchant(any());

        registerer.registerEnchants(new DamageManager(), new BowManager(), customEnchantManager);

        verify(customEnchantManager, atLeast(1)).registerEnchant(any());
    }
}
