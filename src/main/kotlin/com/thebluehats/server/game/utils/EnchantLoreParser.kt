package com.thebluehats.server.game.utils

import org.apache.commons.lang.StringUtils
import java.util.*

class EnchantLoreParser(lore: String = "") : LoreParserBase(lore) {
    private val extraStrings = ArrayList<String>()

    private var variableMatrix: Array<Array<String>?>? = null
    private var singleVariable: Array<String>? = null

    private var onlyOneVariable = false
    private var level = 1

    fun addTextIf(condition: Boolean, text: String) {
        if (!condition) return
        extraStrings.add(text)
    }

    fun parseForLevel(level: Int): ArrayList<String> {
        this.level = level
        val finalLore = StringBuilder(lore)
        
        extraStrings.forEach { a -> finalLore.append(a) }

        return parseLore(finalLore.toString())
    }

    fun setSingleVariable(levelOne: String, levelTwo: String, levelThree: String) {
        onlyOneVariable = true
        singleVariable = arrayOf(levelOne, levelTwo, levelThree)
    }

    fun setVariables(variables: Array<Array<String>?>) {
        variableMatrix = variables
    }

    override fun insertVariableValuesForLine(line: String): String {
        if (variableMatrix == null && singleVariable == null) return line

        var formattedLine = line

        if (onlyOneVariable) {
            formattedLine = StringUtils.replace(formattedLine, "{0}", singleVariable!![level - 1])
        } else {
            variableMatrix?.indices!!.forEach { i ->
                val variable = if (onlyOneVariable) singleVariable!![level - 1] else variableMatrix!![i]?.get(level - 1)
                formattedLine = StringUtils.replace(formattedLine, "{$i}", variable)
            }
        }

        return formattedLine
    }
}