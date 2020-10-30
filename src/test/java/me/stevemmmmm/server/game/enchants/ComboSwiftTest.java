package me.stevemmmmm.server.game.enchants;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ComboSwiftTest {
    @Mock
    private Player player;
    @Mock
    private HitCounter hitCounter;

    private ComboSwift comboSwift;

    private final int HITS_NEEDED = 5;
    private final int SPEED_TIME = 2;
    private final int AMPLIFIER = 1;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        comboSwift = new ComboSwift(hitCounter);
    }

    @Test
    public void SpeedGivenIfHitsNeededAreReached() {
        when(hitCounter.hasHits(player, HITS_NEEDED)).thenReturn(true);

        comboSwift.executeEnchant(player, HITS_NEEDED, SPEED_TIME, AMPLIFIER);

        verify(player).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_TIME * 20, AMPLIFIER, true));
    }

    @Test
    public void SpeedNotGivenIfHitsNeededAreNotReached() {
        comboSwift.executeEnchant(player, HITS_NEEDED, SPEED_TIME, AMPLIFIER);

        verify(player, never()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, SPEED_TIME * 20, AMPLIFIER, true));
    }
}
