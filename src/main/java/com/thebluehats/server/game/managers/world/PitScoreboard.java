package com.thebluehats.server.game.managers.world;

import com.thebluehats.server.api.daos.PerformanceStatsService;
import com.thebluehats.server.game.managers.combat.CombatManager;
import com.thebluehats.server.game.managers.combat.CombatStatus;
import com.thebluehats.server.game.managers.enchants.GlobalTimerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PitScoreboard implements Listener, GlobalTimerListener {
    private final CombatManager combatManager;
    private final PerformanceStatsService performanceStatsService;

    @Inject
    public PitScoreboard(CombatManager combatManager, PerformanceStatsService performanceStatsService) {
        this.combatManager = combatManager;
        this.performanceStatsService = performanceStatsService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        buildScoreboard(event.getPlayer());
    }

    public void buildScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Board optimizedBoard = new Board(scoreboard);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        Date date = new Date();

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        optimizedBoard.title(ChatColor.YELLOW.toString() + ChatColor.BOLD + "THE BLUE HATS PIT");
        optimizedBoard.line(appendColors(ChatColor.GRAY + simpleDateFormat.format(date) + " " + ChatColor.DARK_GRAY + "mega69L"));
        optimizedBoard.line(" ");

        optimizedBoard.line(formatLine("Prestige", ChatColor.YELLOW, "XXXV", false));
        optimizedBoard.line(formatLine("Level", ChatColor.AQUA, "[120]", true));
        optimizedBoard.line(formatLine("XP", ChatColor.AQUA, "MAXED!", false));
        optimizedBoard.line("  ");

        optimizedBoard.line(formatLine("Gold", ChatColor.GOLD, decimalFormat.format(performanceStatsService.getPlayerGold(player)), false));
        optimizedBoard.line("   ");

        optimizedBoard.line(formatLine("Status", null, formatStatus(player), false));
        optimizedBoard.line("    ");

        optimizedBoard.line(appendColors(ChatColor.YELLOW + "play.thebluehatspit.net"));

        player.setScoreboard(scoreboard);
    }

    private String formatLine(String key, ChatColor color, String value, boolean isBold) {
        String finalColor = color != null ? color.toString() : "";
        String boldColor = isBold ? ChatColor.BOLD.toString() : "";

        String valueColor = finalColor + boldColor;

        return appendColors(ChatColor.WHITE + key + ": " + valueColor + value);
    }

    private String appendColors(String string) {
        if (string.length() >= 16) {
            String base = string.substring(0, 16);
            String end = string.substring(16);

            ArrayList<String> colors = new ArrayList<>();

            for (int i = base.length() - 1; i >= 0; i--) {
                if (base.charAt(i) == ChatColor.COLOR_CHAR) {
                    colors.add(base.substring(i, i + 2));

                    int charCheck = i - 2;

                    if (charCheck < 0) continue;

                    if (base.charAt(charCheck) != ChatColor.COLOR_CHAR) break;
                }
            }

            StringBuilder modifiers = new StringBuilder();
            int colorsSize = colors.size();

            for (int i = 0; i < colorsSize - 1; i++) {
                modifiers.append(colors.get(i));
            }

            return base + colors.get(colorsSize - 1) + modifiers + end;
        }

        return string;
    }

    private String formatStatus(Player player) {
        CombatStatus combatStatus = combatManager.getStatus(player);
        String formattedStatus = combatStatus.getFormattedStatus();

        return combatStatus == CombatStatus.COMBAT ?
            formattedStatus + " " + ChatColor.RESET + ChatColor.GRAY + "(" + combatManager.getCombatTime(player) + ")" :
            formattedStatus;
    }

    @Override
    public void onTick(Player player) {
        buildScoreboard(player);
    }

    @Override
    public long getTickDelay() {
        return 20L;
    }
}
