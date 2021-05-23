package com.thebluehats.server.game.utils

import org.apache.commons.lang.StringUtils
import java.util.*

class BasicLoreParser(lore: String) : LoreParser(lore) {
    private var variables: Array<String>? = null

    fun parse(): ArrayList<String> {
        return parseLore(lore)
    }

    fun setVariables(variables: Array<String>) {
        this.variables = variables
    }

    override fun expandTemplates(line: String): String {
        if (variables == null) return line
        var formattedLine = line

        variables!!.indices.forEach { i ->
            formattedLine = StringUtils.replace(formattedLine, "{$i}", variables!![i])
        }

        return formattedLine
    }
}