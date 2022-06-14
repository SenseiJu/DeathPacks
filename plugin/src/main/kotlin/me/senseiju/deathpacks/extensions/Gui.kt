package me.senseiju.deathpacks.extensions

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.BaseGui
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.PaginatedGui
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.sentils.extensions.primitives.color
import me.senseiju.sentils.runnables.newRunnable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin

val BLACK_FILLER_PANE = ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE)
    .name(Component.text(" "))
    .asGuiItem()

class ConfirmationGuiBuilder(title: Component) {

    constructor(title: String) : this(Component.text(title.color()))

    private val gui = Gui.gui()
        .rows(3)
        .disableAllInteractions()
        .title(title)
        .create()

    private val confirmItem = ItemBuilder.from(Material.GREEN_WOOL)
        .name(Component.text("Confirm", NamedTextColor.GREEN, TextDecoration.BOLD))
        .asGuiItem()
    private val declineItem = ItemBuilder.from(Material.RED_WOOL)
        .name(Component.text("Decline", NamedTextColor.RED, TextDecoration.BOLD))
        .asGuiItem()

    init {
        gui.filler.fill(BLACK_FILLER_PANE)

        gui.setItem(2, 4, confirmItem)
        gui.setItem(2, 6, declineItem)
    }

    fun open(player: HumanEntity) {
        gui.openNextTick(player)
    }

    fun onConfirm(action: () -> Unit): ConfirmationGuiBuilder {
        confirmItem.setAction { action() }

        return this
    }

    fun setConfirmLore(vararg lore: String): ConfirmationGuiBuilder {
        return setConfirmLore(lore.toList())
    }

    fun setConfirmLore(lore: List<String>): ConfirmationGuiBuilder {
        confirmItem.itemStack.useItemMeta { meta ->
            meta.lore = lore
        }

        return this
    }

    fun onDecline(action: () -> Unit): ConfirmationGuiBuilder {
        declineItem.setAction { action() }

        return this
    }

    fun setDeclineLore(vararg lore: String): ConfirmationGuiBuilder {
        return setDeclineLore(lore.toList())
    }

    fun setDeclineLore(lore: List<String>): ConfirmationGuiBuilder {
        declineItem.itemStack.useItemMeta { meta ->
            meta.lore = lore
        }

        return this
    }

    fun onClose(action: (InventoryCloseEvent) -> Unit): ConfirmationGuiBuilder {
        gui.setCloseGuiAction {
            action(it)
        }

        return this
    }
}

fun BaseGui.openNextTick(player: HumanEntity) {
    newRunnable {
        open(player)
    }.runTask(JavaPlugin.getPlugin(DeathPacks::class.java))
}

fun PaginatedGui.addPaginationBar(): PaginatedGui {
    filler.fillBetweenPoints(rows, 0, rows, 9, BLACK_FILLER_PANE)

    setItem(rows, 3,
        ItemBuilder.from(Material.PAPER)
            .name(Component.text("Previous", NamedTextColor.BLUE, TextDecoration.BOLD))
            .asGuiItem { previous() })
    setItem(rows, 7,
        ItemBuilder.from(Material.PAPER)
            .name(Component.text("Next", NamedTextColor.BLUE, TextDecoration.BOLD))
            .asGuiItem { next() })

    return this
}