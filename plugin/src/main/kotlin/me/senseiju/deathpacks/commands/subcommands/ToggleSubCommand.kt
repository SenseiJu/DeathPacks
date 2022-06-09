package me.senseiju.deathpacks.commands.subcommands

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Permission
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.senseiju.deathpacks.PERMISSION_USE
import me.senseiju.deathpacks.commands.COMMAND_DEATHPACK_NAME
import me.senseiju.deathpacks.extensions.toColorText
import me.senseiju.deathpacks.storage.CachedStorage
import me.senseiju.sentils.Set
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.entity.Player

@Command(COMMAND_DEATHPACK_NAME)
class ToggleSubCommand(private val storage: CachedStorage) : CommandBase() {

    @SubCommand("toggle")
    @Permission(PERMISSION_USE)
    fun toggle(sender: Player) {
        val deathPack = storage.getDeathPack(sender.uniqueId) ?: return
        deathPack.enabled = !deathPack.enabled

        sender.sendConfigMessage(
            "COMMAND_TOGGLE",
            Set("{ENABLED}", deathPack.enabled.toColorText("Enabled", "Disabled"))
        )
    }
}