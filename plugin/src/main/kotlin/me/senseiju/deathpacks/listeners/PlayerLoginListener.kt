package me.senseiju.deathpacks.listeners

import me.senseiju.deathpacks.storage.CachedStorage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent

private val COMPONENT_FAILED_TO_LOAD = Component.text("Death pack failed to load")
    .color(NamedTextColor.RED)

class PlayerLoginListener(private val storage: CachedStorage) : Listener {

    @EventHandler
    private fun onPlayerPreLogin(e: AsyncPlayerPreLoginEvent) {
        storage.players[e.uniqueId] = storage.getDeathPack(e.uniqueId) ?: storage.loadDeathPack(e.uniqueId)
    }

    @EventHandler
    private fun onPlayerLogin(e: PlayerLoginEvent) {
        if (!storage.players.containsKey(e.player.uniqueId)) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, COMPONENT_FAILED_TO_LOAD)
            return
        }
    }
}