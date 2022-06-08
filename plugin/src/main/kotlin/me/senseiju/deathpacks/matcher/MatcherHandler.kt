package me.senseiju.deathpacks.matcher

import dev.triumphteam.gui.guis.Gui
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.extensions.addPaginationBar
import me.senseiju.deathpacks.extensions.openNextTick
import me.senseiju.deathpacks.extensions.serializeToString
import me.senseiju.deathpacks.json
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

class MatcherHandler(plugin: DeathPacks) {
    var matchableItems = arrayListOf<MatchableItem>()
        private set

    private val file = File(plugin.dataFolder, "items.json")

    init {
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }

        load()
    }

    private fun load() {
        if (file.exists()) {
            matchableItems = json.decodeFromString(file.readText())
        }
    }

    fun addItem(item: ItemStack): Boolean {
        matchableItems.forEach {
            if (it.item.serializeToString() == item.serializeToString()) {
                return false
            }
        }

        matchableItems.add(MatchableItem(item.clone()))
        return true
    }

    fun match(item: ItemStack): Boolean {
        matchableItems.forEach {
            if (it.match(item)) {
                return true
            }
        }

        return false
    }

    fun save() {
        if (file.exists()) {
            file.writeText(json.encodeToString(matchableItems))
        }
    }

    fun openGui(player: Player) {
        val gui = Gui.paginated()
            .rows(4)
            .pageSize(27)
            .disableAllInteractions()
            .title(Component.text("Item Matcher Config"))
            .create()
            .addPaginationBar()

        matchableItems.forEach { gui.addItem(it.asGuiItem(this, player)) }

        gui.openNextTick(player)
    }
}