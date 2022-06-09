package me.senseiju.deathpacks

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import kotlinx.serialization.Serializable
import me.senseiju.deathpacks.api.DeathPack
import me.senseiju.deathpacks.extensions.openNextTick
import me.senseiju.deathpacks.serializers.ItemStackSerializer
import me.senseiju.sentils.extensions.entity.addItemOrDropNaturally
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

private const val MAX_SIZE = 54

@Serializable
class DeathPackImpl(
    private val items: ArrayList<@Serializable(ItemStackSerializer::class) ItemStack>,
    var enabled: Boolean
) : DeathPack {

    companion object {
        fun new(): DeathPackImpl {
            return DeathPackImpl(arrayListOf(), true)
        }
    }

    fun openGui(player: Player) {
        val gui = Gui.storage()
            .rows(6)
            .disableAllInteractions()
            .title(Component.text("Your Death Pack"))
            .create()

        gui.addItem(*items.map { item ->
            ItemBuilder.from(item.clone()).asGuiItem {
                if (items.remove(item)) {
                    player.inventory.addItemOrDropNaturally(item)
                    gui.updateItem(it.slot, ItemStack(Material.AIR))
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
                }
            }
        }.toTypedArray())

        gui.openNextTick(player)
    }

    override fun isFull(): Boolean {
        return items.size >= MAX_SIZE
    }

    override fun hasItems(): Boolean {
        return items.isNotEmpty()
    }

    override fun addItem(item: ItemStack): Boolean {
        if (isFull()) {
            return false
        }

        items.add(item)

        return true
    }

    override fun clearItems(): List<ItemStack> {
        val clonedItems = items.toList()

        items.clear()

        return clonedItems
    }
}