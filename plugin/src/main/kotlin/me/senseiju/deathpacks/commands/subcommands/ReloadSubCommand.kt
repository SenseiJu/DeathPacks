package me.senseiju.deathpacks.commands.subcommands

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.PERMISSION_CONFIG
import me.senseiju.deathpacks.commands.COMMAND_DEATHPACK_NAME
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.entity.Player

@Command(COMMAND_DEATHPACK_NAME)
class ReloadSubCommand(private val plugin: DeathPacks) : CommandBase() {

    @SubCommand("reload")
    @Permission(PERMISSION_CONFIG)
    fun reload(sender: Player) {
        plugin.reload()

        sender.sendConfigMessage("COMMAND_RELOAD_WARNING")
    }
}