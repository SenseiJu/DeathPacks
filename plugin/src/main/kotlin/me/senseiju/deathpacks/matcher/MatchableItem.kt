package me.senseiju.deathpacks.matcher

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import kotlinx.serialization.Serializable
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.extensions.*
import me.senseiju.deathpacks.serializers.ItemStackSerializer
import me.senseiju.sentils.extensions.color
import me.senseiju.sentils.extensions.primitives.color
import me.senseiju.sentils.runnables.newRunnable
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

private val confirmLore = listOf(
    "",
    "&7This will delete the item from DeathPack's matching list",
    "&7and no longer be detected when the player dies.",
    "",
    "&c&lWARNING &7This action is irreversible")
    .color()

private val declineLore = listOf("",
    "&7This will keep the item in the matcher and continue",
    "&7matching the item when the player dies")
    .color()

private val itemClickLore = listOf("",
    "&fLeft-Click &7to edit matching options",
    "&fRight-Click &7to remove item")
    .color()

@Serializable
data class MatchableItem(
    @Serializable(ItemStackSerializer::class) val item: ItemStack,
    private val matchingOptions: MatchingOptions = MatchingOptions.default()
) {

    fun match(targetItem: ItemStack): Boolean {
        if (targetItem.type != item.type) {
            return false
        }

        return if (matchingOptions.name && !isNameMatch(targetItem)) {
            false
        } else if (matchingOptions.enchantments && !isEnchantmentsMatch(targetItem)) {
            false
        } else !(matchingOptions.lore && !isLoreMatch(targetItem))
    }

    fun asGuiItem(matcherHandler: MatcherHandler, player: Player): GuiItem {
        val clonedItem = item.clone()
        clonedItem.useItemMeta {
            it.addLore(itemClickLore)
            it.addLore(
                "",
                "&bName: ${matchingOptions.name.toColorText()}".color(),
                "&bEnchantments: ${matchingOptions.enchantments.toColorText()}".color(),
                "&bLore: ${matchingOptions.lore.toColorText()}".color()
            )
        }

        return ItemBuilder.from(clonedItem)
            .asGuiItem {
                if (it.isLeftClick) {
                    openEditOptionsGui(matcherHandler, player)
                } else if (it.isRightClick) {
                    ConfirmationGuiBuilder("Delete Item")
                        .setConfirmLore(confirmLore)
                        .onConfirm {
                            matcherHandler.matchableItems.remove(this)
                            matcherHandler.openGui(player)
                        }
                        .setDeclineLore(declineLore)
                        .onDecline { player.closeInventory() }
                        .onClose {
                            matcherHandler.openGui(player)
                        }
                        .open(player)
                }
            }
    }

    private fun openEditOptionsGui(matcherHandler: MatcherHandler, player: Player) {
        val gui = Gui.gui()
            .disableAllInteractions()
            .rows(1)
            .title(Component.text("Item Options Config"))
            .create()

        gui.addItem(*matchingOptions.createGuiItemOptions())
        gui.setCloseGuiAction {
            newRunnable {
                matcherHandler.openGui(player)
            }.runTaskLater(JavaPlugin.getPlugin(DeathPacks::class.java), 1)
        }
        gui.openNextTick(player)
    }

    private fun isNameMatch(targetItem: ItemStack): Boolean {
        val targetItemMeta = targetItem.itemMeta!!
        val matchingItemMeta = item.itemMeta!!

        if (!matchingItemMeta.hasDisplayName() && !targetItemMeta.hasDisplayName()) {
            return true
        } else if (matchingItemMeta.hasDisplayName() && targetItemMeta.hasDisplayName()) {
            return matchingItemMeta.displayName == targetItemMeta.displayName
        }

        return false
    }

    private fun isEnchantmentsMatch(targetItem: ItemStack): Boolean {
        val targetItemEnchants = targetItem.itemMeta!!.enchants
        val matchingItemEnchants = item.itemMeta!!.enchants

        if (targetItemEnchants.size != matchingItemEnchants.size) {
            return false
        }

        val nonMatchingEnchants = matchingItemEnchants.filter {
            if (!targetItemEnchants.containsKey(it.key)) {
                return@filter true
            }

            if (targetItemEnchants[it.key] != it.value) {
                return@filter true
            }

            return@filter false
        }

        return nonMatchingEnchants.isEmpty()
    }

    private fun isLoreMatch(targetItem: ItemStack): Boolean {
        val targetItemMeta = targetItem.itemMeta!!
        val matchingItemMeta = item.itemMeta!!

        if (!matchingItemMeta.hasLore() && !targetItemMeta.hasLore()) {
            return true
        } else if (matchingItemMeta.hasLore() && targetItemMeta.hasLore()) {
            val itemLore = matchingItemMeta.lore!!
            val targetItemLore = targetItemMeta.lore!!
            if (itemLore.size != targetItemLore.size) {
                return false
            }

            itemLore.zip(targetItemLore).forEach { (c1, c2) ->
                if (c1 == c2) {
                    return false
                }
            }

            return true
        }

        return false
    }
}