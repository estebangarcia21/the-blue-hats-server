/*
 * This class is taken from Exerosis's Board.java gist.
 * https://gist.github.com/Exerosis/f422c17dde154cca65cde4c4e4b43ca3
 *
 * This is NOT THE WORK of The Blue Hats development team. All credit goes to https://gist.github.com/Exerosis.
 *
 * This file was slightly modified by The Blue Hats development team.
 */

package com.thebluehats.server.game.managers.world;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.NoSuchElementException;

import static org.bukkit.Bukkit.getScoreboardManager;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.COLOR_CHAR;
import static org.bukkit.scoreboard.DisplaySlot.SIDEBAR;

/**
 * A 32 character no flicker scoreboard implementation, fast and lightweight.
 */
public class Board {
    private static final int MAX_LINES = 15;
    private static final int MAX_CHARACTERS;
    private static final String[] BLANKS = new String[MAX_LINES];
    private final Team[] teams = new Team[MAX_LINES];
    private final Objective objective;

    static {
        MAX_CHARACTERS = !getServer().getVersion().contains("1.1") ? 16 : 64;

        for (int i = 0; i < MAX_LINES; ++i)
            BLANKS[i] = new String(new char[]{COLOR_CHAR, (char) ('s' + i)});
    }

    /**
     * Construct a new {@link Scoreboard} wrapping the given {@link org.bukkit.scoreboard.Scoreboard}.
     *
     * @param board
     * 		- The {@link org.bukkit.scoreboard.Scoreboard} to wrap.
     */
    public Board(Scoreboard board) {
        board.clearSlot(SIDEBAR);
        objective = board.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(SIDEBAR);
        for (int i = 0; i < MAX_LINES; i++)
            (teams[i] = board.registerNewTeam(BLANKS[i])).addEntry(BLANKS[i]);
    }

    /**
     * Construct a new {@link Scoreboard} wrapping the main {@link org.bukkit.scoreboard.Scoreboard}.
     */
    public Board() { this(getScoreboardManager().getMainScoreboard()); }

    /**
     * Sets the title to the given value.
     *
     * @param title
     * 		- The new title.
     */
    public void title(Object title) { objective.setDisplayName(title.toString()); }

    /**
     * Sets the line at a given index to the given text and score.
     *
     * @param index
     * 		- The index of the line.
     * @param text
     * 		- The text 32-128 characters max.
     * @param score
     * 		- The score to display for this line.
     * @return - The index of the line.
     */
    public int line(int index, String text, int score) {
        final int max = MAX_CHARACTERS, min = MAX_CHARACTERS - 1;
        final int split = text.length() < max ? 0 : text.charAt(min) == '§' ? min : max;

        teams[index].setPrefix(split == 0 ? text : text.substring(0, split));
        teams[index].setSuffix(split == 0 ? "" : text.substring(split));

        objective.getScore(BLANKS[index]).setScore(score);
        return index;
    }

    /**
     * Sets the line at a given index to the given text with a score of 1.
     *
     * @param index
     * 		- The index of the line.
     * @param text
     * 		- The text. ~32 characters max.
     * @return - The index of the line.
     */
    public int line(int index, String text) { return line(index, text, MAX_LINES - index); }

    /**
     * Sets the first empty line to the given text with a score of 1.
     *
     * @param text
     * 		- The text. ~32 characters max.
     * @return - The index of the line.
     */
    public int line(String text) {
        for (int i = 0; i < MAX_LINES; i++)
            if (teams[i].getPrefix().isEmpty())
                return line(i, text);
        throw new NoSuchElementException("No empty lines");
    }


    /**
     * Removes the line at the given index.
     *
     * @param index
     * 		- The index of the line.
     * @return - {@code true} if the line was previously set.
     */
    public boolean remove(int index) {
        if (index >= MAX_LINES) return false;
        objective.getScoreboard().resetScores(BLANKS[index]);
        return true;
    }
}
