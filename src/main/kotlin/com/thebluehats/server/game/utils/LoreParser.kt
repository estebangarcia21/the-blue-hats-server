package com.thebluehats.server.game.utils

import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import java.util.*

abstract class LoreParser protected constructor(protected val lore: String) {
    /**
     * Replaces HTML color tags with the specified Minecraft color chat code.
     * Example: `<red>This is red chat!</red>`
     */
    protected fun parseLore(lore: String): ArrayList<String> {
        val parsedLore = ArrayList<String>()
        val lines = lore.split("<br/>").toTypedArray()

        lines.forEach { l ->
            var formattedLine = expandTemplates(ChatColor.GRAY.toString() + l)

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

    /**
     * Replaces the variable templates in one lore line.
     * For example `Hello {0}!`
     */
    protected abstract fun expandTemplates(line: String): String
}
