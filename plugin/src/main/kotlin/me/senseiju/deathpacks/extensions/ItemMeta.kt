package me.senseiju.deathpacks.extensions

import org.bukkit.inventory.meta.ItemMeta

fun ItemMeta.addLore(strings: List<String>) {
    if (!hasLore()) {
        lore = strings
    } else {
        lore!!.let { newLore ->
            newLore.addAll(strings)
            lore = newLore
        }
    }
}

fun ItemMeta.addLore(vararg string: String) {
    addLore(string.toList())
}