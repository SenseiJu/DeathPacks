package me.senseiju.deathpacks.extensions

fun Boolean.toColorText(): String {
    return toColorText("True", "False")
}

fun Boolean.toColorText(t1: String, t2: String): String {
    return if (this) "&a&l$t1" else "&c&l$t2"
}