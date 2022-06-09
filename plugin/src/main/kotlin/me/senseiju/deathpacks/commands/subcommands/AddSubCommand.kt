package me.senseiju.deathpacks.commands.subcommands

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.senseiju.deathpacks.PERMISSION_CONFIG
import me.senseiju.deathpacks.commands.COMMAND_DEATHPACK_NAME
import me.senseiju.deathpacks.matcher.MatcherHandler
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.Material
import org.bukkit.entity.Player

@Command(COMMAND_DEATHPACK_NAME)
class AddSubCommand(private val matcherHandler: MatcherHandler) : CommandBase() {

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
}