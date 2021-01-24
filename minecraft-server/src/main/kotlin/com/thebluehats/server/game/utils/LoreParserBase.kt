package com.thebluehats.server.game.utils

import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import java.util.*

abstract class LoreParserBase protected constructor(protected val lore: String) {
    protected fun parseLore(lore: String): ArrayList<String> {
        val parsedLore = ArrayList<String>()
        val lines = lore.split("<br/>").toTypedArray()

        lines.forEach { l ->
            var formattedLine = insertVariableValuesForLine(ChatColor.GRAY.toString() + l)

            ChatColor.values().forEach { c ->
                val name = c.name.toLowerCase()

                formattedLine = StringUtils.replace(formattedLine, "-","_" )
                formattedLine = StringUtils.replace(formattedLine, "<$name>", c.toString())
                formattedLine = StringUtils.replace(formattedLine, "</$name>", ChatColor.GRAY.toString())
            }
            
            parsedLore.add(formattedLine)
        }

        return parsedLore
    }

    protected abstract fun insertVariableValuesForLine(line: String): String
}