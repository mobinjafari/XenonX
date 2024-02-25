package org.lotka.xenonx.util.extension

fun String.isNumeric(): Boolean {
    return this.matches(Regex("\\d+"))
}