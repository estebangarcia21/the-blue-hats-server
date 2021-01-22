package com.thebluehats.server.game.utils

import org.apache.commons.lang.StringUtils
import java.util.*

class LoreParser(lore: String) : LoreParserBase<Array<String>?>(lore) {
    private var variables: Array<String>? = null

    fun parse(): ArrayList<String> {
        return parseLore(lore)
    }

    override fun setVariables(variables: Array<String>?) {
        this.variables = variables
    }

    override fun insertVariableValuesForLine(line: String): String {
        if (variables == null) return line
        var formattedLine = line

        for (i in variables!!.indices) {
            formattedLine = StringUtils.replace(formattedLine, "{$i}", variables!![i])
        }

        return formattedLine
    }
}