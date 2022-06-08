package me.senseiju.deathpacks

import kotlinx.serialization.json.Json
import me.mattstudios.mf.base.CommandManager
import me.senseiju.deathpacks.api.DeathPacksProvider
import me.senseiju.deathpacks.commands.DeathPackCommand
import me.senseiju.deathpacks.listeners.PlayerDeathListener
import me.senseiju.deathpacks.listeners.PlayerLoginListener
import me.senseiju.deathpacks.matcher.MatcherHandler
import me.senseiju.deathpacks.storage.CachedStorage
import me.senseiju.deathpacks.storage.implementation.JSONStorage
import me.senseiju.sentils.extensions.MessageProvider
import me.senseiju.sentils.registerEvents
import me.senseiju.sentils.storage.ConfigFile
import org.bukkit.plugin.java.JavaPlugin

val json = Json { prettyPrint = true }

class DeathPacks : JavaPlugin() {
    lateinit var config: ConfigFile
    lateinit var storage: CachedStorage

    private lateinit var matcherHandler: MatcherHandler
    private lateinit var commandManager: CommandManager

    override fun onEnable() {
        config = ConfigFile(this, "config.yml", true)
        MessageProvider.messages = ConfigFile(this, "messages.yml", true)

        storage = JSONStorage(this)
        matcherHandler = MatcherHandler(this)
        commandManager = CommandManager(this)

        DeathPacksProvider.set(DeathPacksAPIImpl(storage))

        registerEvents(
            PlayerLoginListener(storage),
            PlayerDeathListener(matcherHandler)
        )

        commandManager.register(DeathPackCommand(storage, matcherHandler))
    }

    override fun onDisable() {
        storage.cancel()

        matcherHandler.save()
    }
}