package me.senseiju.deathpacks.commands

import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import me.senseiju.deathpacks.PERMISSION_CONFIG
import me.senseiju.deathpacks.PERMISSION_USE
import me.senseiju.deathpacks.matcher.MatcherHandler
import me.senseiju.deathpacks.storage.CachedStorage
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.Material
import org.bukkit.entity.Player

@Command("deathpack")
@Alias("deathpacks", "dp")
class DeathPackCommand(private val storage: CachedStorage, private val matcherHandler: MatcherHandler) : CommandBase() {

    @Default
    @Permission(PERMISSION_USE)
    fun default(sender: Player) {
        storage.getDeathPack(sender.uniqueId)?.openGui(sender)
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
            sender.sendConfigMessage("COMMAND_DEATHPACKS_ADD_NO_ITEM_IN_HAND")
            return
        }

        if (matcherHandler.addItem(sender.inventory.itemInMainHand)) {
            sender.sendConfigMessage("COMMAND_DEATHPACKS_ADD_SUCCESS")
        } else {
            sender.sendConfigMessage("COMMAND_DEATHPACKS_ADD_ITEM_ALREADY_ADDED")
        }
    }
}