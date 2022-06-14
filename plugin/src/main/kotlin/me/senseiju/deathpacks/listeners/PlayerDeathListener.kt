package me.senseiju.deathpacks.listeners

import me.senseiju.deathpacks.DeathPackImpl
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.PERMISSION_USE
import me.senseiju.deathpacks.api.DeathPacksProvider
import me.senseiju.deathpacks.matcher.MatcherHandler
import me.senseiju.sentils.Set
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.plugin.java.JavaPlugin

private val plugin = JavaPlugin.getPlugin(DeathPacks::class.java)

class PlayerDeathListener(private val matcherHandler: MatcherHandler) : Listener {
    private val api = DeathPacksProvider.get()

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private fun onPlayerDeath(e: PlayerDeathEvent) {
        val player = e.entity
        val killer = e.entity.killer ?: return
        if (!killer.isOnline || !killer.hasPermission(PERMISSION_USE)) {
            return
        }

        val disabledWorlds = plugin.config.getStringList("disabled-worlds")
        if (disabledWorlds.contains(player.location.world?.name) || disabledWorlds.contains(killer.location.world?.name)) {
            return
        }

        e.drops.addAll(api.getDeathPack(player.uniqueId)?.clearItems() ?: emptyList())

        val deathPack = (api.getDeathPack(killer.uniqueId) ?: return) as DeathPackImpl
        if (!deathPack.enabled) {
            return
        }

        var itemsAdded = 0
        e.drops.removeAll { item ->
            if (matcherHandler.match(item) && deathPack.addItem(item)) {
                itemsAdded += 1
                return@removeAll true
            }

            return@removeAll false
        }

        if (itemsAdded > 0) {
            killer.sendConfigMessage("ITEMS_ADDED", Set("{NUM_OF_ITEMS}", itemsAdded))
        }
    }
}