package com.thebluehats.server.game.utils

import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import java.util.*

abstract class LoreParserBase<V> protected constructor(protected val lore: String) {
    protected fun parseLore(lore: String): ArrayList<String> {
        val parsedLore = ArrayList<String>()
        val lines = lore.split("<br/>").toTypedArray()

        for (line in lines) {
            var formattedLine = ChatColor.GRAY.toString() + line
            formattedLine = insertVariableValuesForLine(formattedLine)
            for (chatColor in ChatColor.values()) {
                val name = chatColor.name.toLowerCase()
                formattedLine = StringUtils.replace(formattedLine, "<$name>", chatColor.toString())
                formattedLine = StringUtils.replace(formattedLine, "</$name>", ChatColor.GRAY.toString())
            }
            parsedLore.add(formattedLine)
        }

        return parsedLore
    }

    abstract fun setVariables(variables: V)
    protected abstract fun insertVariableValuesForLine(line: String): String
}