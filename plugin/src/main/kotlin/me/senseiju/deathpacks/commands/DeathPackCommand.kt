package me.senseiju.deathpacks.commands

import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.PERMISSION_CONFIG
import me.senseiju.deathpacks.PERMISSION_USE
import me.senseiju.deathpacks.matcher.MatcherHandler
import me.senseiju.deathpacks.storage.CachedStorage
import me.senseiju.sentils.Set
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

private val plugin = JavaPlugin.getPlugin(DeathPacks::class.java)

@Command("deathpack")
@Alias("deathpacks", "dp")
class DeathPackCommand(private val storage: CachedStorage, private val matcherHandler: MatcherHandler) : CommandBase() {

    @Default
    @Permission(PERMISSION_USE)
    fun default(sender: Player) {
        storage.getDeathPack(sender.uniqueId)?.openGui(sender)
    }

    @SubCommand("toggle")
    @Permission(PERMISSION_USE)
    fun toggle(sender: Player) {
        val deathPack = storage.getDeathPack(sender.uniqueId) ?: return
        deathPack.enabled = !deathPack.enabled

        sender.sendConfigMessage("COMMAND_TOGGLE", Set("{ENABLED}", deathPack.enabled.toColorText()))
    }

    @SubCommand("config")
    @Permission(PERMISSION_CONFIG)
    fun config(sender: Player) {
        matcherHandler.openGui(sender)
    }

    @SubCommand("add")
    @Permission(PERMISSION_CONFIG)
    fun add(sender: Player) {
        if (sender.inventory.itemInMainHand.type == Material.AIR) {
            sender.sendConfigMessage("COMMAND_ADD_NO_ITEM_IN_HAND")
            return
        }

        if (matcherHandler.addItem(sender.inventory.itemInMainHand)) {
            sender.sendConfigMessage("COMMAND_ADD_SUCCESS")
        } else {
            sender.sendConfigMessage("COMMAND_ADD_ITEM_ALREADY_ADDED")
        }
    }

    @SubCommand("reload")
    @Permission(PERMISSION_CONFIG)
    fun reload(sender: Player) {
        plugin.reload()

        sender.sendConfigMessage("COMMAND_RELOAD_WARNING")
    }

    private fun Boolean.toColorText(): String {
        return if (this) "&a&lEnabled" else "&c&lDisabled"
    }
}