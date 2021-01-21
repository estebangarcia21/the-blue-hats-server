package com.thebluehats.server.game.utils;

infix fun Array<Array<String>?>.add(variable: Var) {
    this[variable.index] = arrayOf(variable.one, variable.two, variable.three)
}

fun varMatrix(): Array<Array<String>?> = arrayOfNulls(2)

class Var(val index: Int, val one: String, val two: String, val three: String)
