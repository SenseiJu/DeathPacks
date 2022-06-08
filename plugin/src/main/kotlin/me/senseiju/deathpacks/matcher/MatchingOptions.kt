package me.senseiju.deathpacks.matcher

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.GuiItem
import kotlinx.serialization.Serializable
import me.senseiju.deathpacks.extensions.addLore
import me.senseiju.deathpacks.extensions.useItemMeta
import me.senseiju.sentils.extensions.color
import me.senseiju.sentils.extensions.primitives.color
import net.kyori.adventure.text.Component
import org.bukkit.Material

private val nameOptionLore = listOf(
    "",
    "&7This option will determine if the name will be matched.",
    "&7The names of the item must be an exact match.")
    .color()
    .map { Component.text(it) }


private val enchantmentsOptionLore = listOf(
    "",
    "&7This option will determine if the enchantments will be matched.",
    "&7The order of enchantments do not matters however the level of the,",
    "&7enchant does.")
    .color()
    .map { Component.text(it) }

private val loreOptionLore = listOf(
    "",
    "&7This option will determine if the lore will be matched.",
    "&7The order of lore matters and if its not an exact match,",
    "&7the item will not be matched.")
    .color()
    .map { Component.text(it) }

@Serializable
data class MatchingOptions(
    var name: Boolean,
    var enchantments: Boolean,
    var lore: Boolean
) {
    companion object {
        fun default(): MatchingOptions {
            return MatchingOptions(name = true, enchantments = true, lore = true)
        }
    }

    fun createGuiItemOptions(): Array<GuiItem> {
        return arrayOf(createNameItem(), createEnchantmentsItem(), createLoreItem())
    }

    private fun createNameItem(): GuiItem {
        val guiItem = ItemBuilder.from(name.toGlassPane())
            .asGuiItem { event ->
                name = !name

                event.currentItem?.type = name.toGlassPane()
                event.currentItem?.useItemMeta {
                    it.displayName(Component.text("&bName&f: ${name.toColorText()}".color()))
                }
            }

        guiItem.itemStack.useItemMeta {
            it.displayName(Component.text("&bName&f: ${name.toColorText()}".color()))
            it.addLore(nameOptionLore)
        }

        return guiItem
    }

    private fun createEnchantmentsItem(): GuiItem {
        val guiItem = ItemBuilder.from(enchantments.toGlassPane())
            .asGuiItem { event ->
                enchantments = !enchantments

                event.currentItem?.type = enchantments.toGlassPane()
                event.currentItem?.useItemMeta {
                    it.displayName(Component.text("&bEnchantments&f: ${enchantments.toColorText()}".color()))
                }
            }

        guiItem.itemStack.useItemMeta {
            it.displayName(Component.text("&bEnchantments&f: ${enchantments.toColorText()}".color()))
            it.addLore(enchantmentsOptionLore)
        }

        return guiItem
    }

    private fun createLoreItem(): GuiItem {
        val guiItem = ItemBuilder.from(lore.toGlassPane())
            .asGuiItem { event ->
                lore = !lore

                event.currentItem?.type = lore.toGlassPane()
                event.currentItem?.useItemMeta {
                    it.displayName(Component.text("&bLore&f: ${lore.toColorText()}".color()))
                }
            }

        guiItem.itemStack.useItemMeta {
            it.displayName(Component.text("&bLore&f: ${lore.toColorText()}".color()))
            it.addLore(loreOptionLore)
        }

        return guiItem
    }

    private fun Boolean.toColorText(): String {
        return if (this) "&a&lTrue" else "&c&lFalse"
    }

    private fun Boolean.toGlassPane(): Material {
        return if (this) Material.GREEN_STAINED_GLASS_PANE else Material.RED_STAINED_GLASS_PANE
    }
}
