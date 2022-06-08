package me.senseiju.deathpacks.extensions

import net.kyori.adventure.text.Component
import org.bukkit.inventory.meta.ItemMeta

fun ItemMeta.addLore(components: List<Component>) {
    if (!hasLore()) {
        lore(components)
    } else {
        lore()!!.let { lore ->
            lore.addAll(components)
            lore(lore)
        }
    }
}

fun ItemMeta.addLore(vararg components: Component) {
    addLore(components.toList())
}