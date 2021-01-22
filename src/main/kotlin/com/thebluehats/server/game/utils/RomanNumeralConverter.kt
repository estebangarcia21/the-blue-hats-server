package com.thebluehats.server.game.utils

class RomanNumeralConverter {
    fun convertToRomanNumeral(value: Int): String? {
        when (value) {
            0 -> return "None"
            1 -> return "I"
            2 -> return "II"
            3 -> return "III"
            4 -> return "IV"
            5 -> return "V"
            6 -> return "VI"
            7 -> return "VII"
            8 -> return "VIII"
            9 -> return "IX"
            10 -> return "X"
            11 -> return "XI"
            12 -> return "XII"
            13 -> return "XIII"
            14 -> return "XIV"
            15 -> return "XV"
            16 -> return "XVI"
            17 -> return "XVII"
            18 -> return "XVIII"
            19 -> return "XIX"
            20 -> return "XX"
            21 -> return "XXI"
            22 -> return "XXII"
            23 -> return "XXIII"
            24 -> return "XXIV"
            25 -> return "XXV"
            26 -> return "XXVI"
            27 -> return "XXVII"
            28 -> return "XXVIII"
            29 -> return "XXIX"
            30 -> return "XXX"
            35 -> return "XXXV"
        }

        return null
    }

    fun convertRomanNumeralToInteger(numeral: String?): Int {
        when (numeral) {
            "I" -> return 1
            "II" -> return 2
            "III" -> return 3
        }

        return -1
    }
}