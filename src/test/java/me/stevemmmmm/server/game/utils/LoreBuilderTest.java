package me.stevemmmmm.server.game.utils;

import org.bukkit.ChatColor;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LoreBuilderTest {
    private LoreBuilder loreBuilder = new LoreBuilder();

    private ArrayList<String> description;
    private List<String[]> variables;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        loreBuilder = new LoreBuilder();

        Field descriptionField;
        descriptionField = LoreBuilder.class.getDeclaredField("description");

        descriptionField.setAccessible(true);

        Object descriptionFieldValue = descriptionField.get(loreBuilder);

        if (descriptionFieldValue instanceof ArrayList<?>) {
            @SuppressWarnings("unchecked")
            ArrayList<String> value = (ArrayList<String>) descriptionFieldValue;

            this.description = value;
        }

        Field parametersField;
        parametersField = LoreBuilder.class.getDeclaredField("variables");

        parametersField.setAccessible(true);

        Object parametersFieldValue = parametersField.get(loreBuilder);

        if (parametersFieldValue instanceof ArrayList<?>) {
            @SuppressWarnings("unchecked")
            List<String[]> value = (ArrayList<String[]>) parametersFieldValue;

            this.variables = value;
        }
    }

    private ChatColor getColor() throws IllegalAccessException, NoSuchFieldException {
        Field colorField;
        colorField = LoreBuilder.class.getDeclaredField("color");

        colorField.setAccessible(true);

        return (ChatColor) colorField.get(loreBuilder);
    }

    private boolean getWriteCondition() throws IllegalAccessException, NoSuchFieldException {
        Field conditionField;
        conditionField = LoreBuilder.class.getDeclaredField("condition");

        conditionField.setAccessible(true);

        return (boolean) conditionField.get(loreBuilder);
    }

    @Test
    public void DeclaresVariableAndAddsToParameters() {
        loreBuilder.declareVariable("A", "B", "C");

        assertFalse(variables.isEmpty());
    }

    @Test
    public void WritesParamater() {
        loreBuilder.declareVariable("A", "B", "C").writeVariable(0,1);

        assertTrue(description.contains(ChatColor.GRAY + "A"));
    }

    @Test
    public void WriteVariableWritesParameterWithColor() {
        loreBuilder.declareVariable("A", "B", "C").writeVariable(ChatColor.BLUE, 0,1);

        assertTrue(description.contains(ChatColor.BLUE + "A"));
    }

    @Test
    public void AddsNewLineToDescriptionWhenNext() {
        loreBuilder.write("Something");
        loreBuilder.next();

        assertEquals("", description.get(description.size() - 1));
    }

    @Test
    public void AppendsStringWhenWrite() {
        loreBuilder.write("Something");

        assertTrue(description.contains(ChatColor.GRAY + "Something"));
    }

    @Test
    public void AppendsStringWithColorWhenWrite() {
        loreBuilder.write(ChatColor.BLUE, "Something");

        assertTrue(description.contains(ChatColor.BLUE + "Something"));
    }

    @Test
    public void AppendsStringWhenConditionIsTrue() {
        loreBuilder.writeOnlyIf(true, "Something");

        assertTrue(description.contains(ChatColor.GRAY + "Something"));

        loreBuilder.writeOnlyIf(false, "Something2");

        assertFalse(description.contains(ChatColor.GRAY + "Something2"));
    }

    @Test
    public void SetColorSetsColor() throws NoSuchFieldException, IllegalAccessException {
        loreBuilder.setColor(ChatColor.BLUE);

        assertEquals(ChatColor.BLUE, getColor());
    }

    @Test
    public void ColorIsGrayWhenResetColor() throws NoSuchFieldException, IllegalAccessException {
        loreBuilder.resetColor();

        assertEquals(ChatColor.GRAY, getColor());
    }

    @Test
    public void UpdatesWriteCondition() throws NoSuchFieldException, IllegalAccessException {
        loreBuilder.setWriteCondition(true);

        assertTrue(getWriteCondition());

        loreBuilder.setWriteCondition(false);

        assertFalse(getWriteCondition());
    }

    @Test
    public void SetsWriteConditionToTrueWhenResetCondition() throws NoSuchFieldException, IllegalAccessException {
        loreBuilder.setWriteCondition(false);

        loreBuilder.resetCondition();

        assertTrue(getWriteCondition());
    }
}
