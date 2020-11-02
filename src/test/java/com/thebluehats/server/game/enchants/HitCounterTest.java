package com.thebluehats.server.game.enchants;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.thebluehats.server.core.Main;
import com.thebluehats.server.game.managers.enchants.HitCounter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Bukkit.class })
public class HitCounterTest {
    @Mock
    private Player player;

    private HitCounter counter;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        PowerMockito.mockStatic(Bukkit.class);

        counter = spy(new HitCounter(mock(Main.class)));
    }

    @Test
    public void AddOne() {
        doNothing().when(counter).startHitResetTimer(player);

        counter.addOne(player);

        assertTrue(counter.hasHits(player, 1));
    }

    @Test
    public void Add() {
        doNothing().when(counter).startHitResetTimer(player);

        counter.add(player, 5);

        assertTrue(counter.hasHits(player, 5));
    }

    @Test
    public void HasRequiredHits() {
        doNothing().when(counter).startHitResetTimer(player);

        assertTrue(counter.hasHits(player, 0));
        assertFalse(counter.hasHits(player, 1));
    }

    @Test
    public void HitResetTimer() {
        doNothing().when(counter).startHitResetTimer(player);

        counter.add(player, 5);

        doCallRealMethod().when(counter).startHitResetTimer(player);

        Server server = mock(Server.class);
        BukkitScheduler scheduler = mock(BukkitScheduler.class);

        when(Bukkit.getServer()).thenReturn(server);
        when(server.getScheduler()).thenReturn(scheduler);

        when(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(any(), any(Runnable.class), anyLong(), anyLong())).thenReturn(0);

        counter.startHitResetTimer(player);

        assertTrue(counter.hasHits(player, 0));
    }
}
