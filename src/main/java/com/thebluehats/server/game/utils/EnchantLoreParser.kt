package com.thebluehats.server.game.utils

import org.apache.commons.lang.StringUtils
import java.util.*

class EnchantLoreParser(lore: String = "") : LoreParserBase<Array<Array<String>?>>(lore) {
    private val appensions = ArrayList<String>()

    private var variableMatrix: Array<Array<String>?>? = null
    private var singleVariable: Array<String>? = null

    private var onlyOneVariable = false
    private var level = 1

    fun addTextIf(condition: Boolean, text: String) {
        if (!condition) return
        appensions.add(text)
    }

    fun parseForLevel(level: Int): ArrayList<String> {
        this.level = level
        var finalLore = lore

        for (appension in appensions) {
            finalLore += appension
        }

        return parseLore(finalLore!!)
    }

    fun setSingleVariable(levelOne: String, levelTwo: String, levelThree: String) {
        onlyOneVariable = true
        singleVariable = arrayOf(levelOne, levelTwo, levelThree)
    }

    override fun setVariables(variables: Array<Array<String>?>) {
        variableMatrix = variables
    }

    override fun insertVariableValuesForLine(line: String): String {
        if (variableMatrix == null && singleVariable == null) return line
        var formattedLine = line
        if (onlyOneVariable) {
            formattedLine = StringUtils.replace(formattedLine, "{0}", singleVariable!![level - 1])
        } else {
            for (i in 0 until Objects.requireNonNull(variableMatrix).length) {
                val variable = if (onlyOneVariable) singleVariable!![level - 1] else variableMatrix!![i][level - 1]
                formattedLine = StringUtils.replace(formattedLine, "{$i}", variable)
            }
        }
        return formattedLine
    }
}