package me.senseiju.deathpacks.commands.subcommands

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.senseiju.deathpacks.PERMISSION_CONFIG
import me.senseiju.deathpacks.commands.COMMAND_DEATHPACK_NAME
import me.senseiju.deathpacks.matcher.MatcherHandler
import org.bukkit.entity.Player

@Command(COMMAND_DEATHPACK_NAME)
class ConfigSubCommand(private val matcherHandler: MatcherHandler) : CommandBase() {

    @SubCommand("config")
    @Permission(PERMISSION_CONFIG)
    fun config(sender: Player) {
        matcherHandler.openGui(sender)
    }
}