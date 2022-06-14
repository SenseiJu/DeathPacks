package me.senseiju.deathpacks.listeners

import kotlinx.coroutines.*
import me.senseiju.deathpacks.storage.CachedStorage
import me.senseiju.sentils.extensions.primitives.color
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent

private val COMPONENT_FAILED_TO_LOAD = "&cDeathPack failed to load".color()

class PlayerLoginListener(private val storage: CachedStorage) : Listener {

    @EventHandler
    private fun onPlayerPreLogin(e: AsyncPlayerPreLoginEvent) {
        runBlocking(Dispatchers.IO) {
            storage.players[e.uniqueId] = storage.getDeathPack(e.uniqueId) ?: storage.loadDeathPack(e.uniqueId)
        }
    }

    @EventHandler
    private fun onPlayerLogin(e: PlayerLoginEvent) {
        if (!storage.players.containsKey(e.player.uniqueId)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, COMPONENT_FAILED_TO_LOAD)
            return
        }
    }

    @EventHandler
    private fun onPlayerJoin(e: PlayerJoinEvent) {
        if (storage.players[e.player.uniqueId]?.hasItems() == true) {
            e.player.sendConfigMessage("ITEMS_TO_CLAIM")
        }
    }
}