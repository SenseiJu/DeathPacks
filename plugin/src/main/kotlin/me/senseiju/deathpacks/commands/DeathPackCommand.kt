package me.senseiju.deathpacks.commands

import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.mf.base.CommandManager
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.PERMISSION_USE
import me.senseiju.deathpacks.commands.subcommands.AddSubCommand
import me.senseiju.deathpacks.commands.subcommands.ConfigSubCommand
import me.senseiju.deathpacks.commands.subcommands.ReloadSubCommand
import me.senseiju.deathpacks.commands.subcommands.ToggleSubCommand
import org.bukkit.entity.Player

const val COMMAND_DEATHPACK_NAME = "deathpack"

@Command(COMMAND_DEATHPACK_NAME)
@Alias("deathpacks", "dp")
class DeathPackCommand(plugin: DeathPacks, commandManager: CommandManager) : CommandBase() {
    private val storage = plugin.storage

    init {
        // Register default command
        commandManager.register(this)

        // Register admin subcommands
        commandManager.register(
            AddSubCommand(plugin.matcherHandler),
            ConfigSubCommand(plugin.matcherHandler),
            ReloadSubCommand(plugin)
        )

        // Register player subcommands
        commandManager.register(
            ToggleSubCommand(plugin.storage)
        )
    }

    @Default
    @Permission(PERMISSION_USE)
    fun default(sender: Player) {
        storage.getDeathPack(sender.uniqueId)?.openGui(sender)
    }
}