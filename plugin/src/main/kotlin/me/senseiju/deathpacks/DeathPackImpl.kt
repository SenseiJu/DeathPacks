package me.senseiju.deathpacks

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import kotlinx.serialization.Serializable
import me.senseiju.deathpacks.api.DeathPack
import me.senseiju.deathpacks.extensions.addPaginationBar
import me.senseiju.deathpacks.extensions.openNextTick
import me.senseiju.deathpacks.serializers.ItemStackSerializer
import me.senseiju.sentils.extensions.entity.addItemOrDropNaturally
import me.senseiju.sentils.serializers.UUIDSerializer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

private val plugin = JavaPlugin.getPlugin(DeathPacks::class.java)

@Serializable
class DeathPackImpl(
    @Serializable(UUIDSerializer::class) val uuid: UUID,
    private val items: ArrayList<@Serializable(ItemStackSerializer::class) ItemStack>
) : DeathPack {
    private var maxSize = 0

    init {
        updateMaxSize()
    }

    companion object {
        fun new(uuid: UUID): DeathPackImpl {
            return DeathPackImpl(uuid, arrayListOf())
        }
    }

    fun openGui(player: Player) {
        val gui = Gui.paginated()
            .rows(4)
            .pageSize(27)
            .disableAllInteractions()
            .title(Component.text("Your Death Pack"))
            .create()
            .addPaginationBar()

        gui.addItem(*items.map { item ->
            ItemBuilder.from(item).asGuiItem {
                if (items.remove(item)) {
                    player.inventory.addItemOrDropNaturally(item)
                    gui.updatePageItem(it.slot, ItemStack(Material.AIR))
                    player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F)
                }
            }
        }.toTypedArray())

        gui.openNextTick(player)
    }

    fun updateMaxSize() {
        val player = Bukkit.getPlayer(uuid) ?: return
        val sizeLimitsConfig = plugin.config.getConfigurationSection("sizeLimits") ?: return
        sizeLimitsConfig.getKeys(false).forEach {
            if (player.hasPermission(PERMISSION_SIZE.replace("{num}", sizeLimitsConfig.getInt(it).toString()))) {
                if (sizeLimitsConfig.getInt(it) > maxSize) {
                    maxSize = sizeLimitsConfig.getInt(it)
                }
            }
        }
    }

    override fun isFull(): Boolean {
        Bukkit.getPlayer(uuid) ?: return true

        return items.size >= maxSize
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