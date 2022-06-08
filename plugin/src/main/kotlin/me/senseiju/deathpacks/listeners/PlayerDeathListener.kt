package me.senseiju.deathpacks.listeners

import me.senseiju.deathpacks.DeathPackImpl
import me.senseiju.deathpacks.PERMISSION_USE
import me.senseiju.deathpacks.api.DeathPacksProvider
import me.senseiju.deathpacks.matcher.MatcherHandler
import me.senseiju.sentils.Set
import me.senseiju.sentils.extensions.sendConfigMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(private val matcherHandler: MatcherHandler) : Listener {
    private val api = DeathPacksProvider.get()

    @EventHandler
    private fun onPlayerDeath(e: PlayerDeathEvent) {
        e.drops.addAll(api.getDeathPack(e.entity.uniqueId)?.clearItems() ?: emptyList())

        val killer = e.entity.killer ?: return
        if (!killer.isOnline || !killer.hasPermission(PERMISSION_USE)) {
            return
        }

        var itemsAdded = 0
        val deathPack = (api.getDeathPack(killer.uniqueId) ?: return) as DeathPackImpl
        e.drops.removeAll { item ->
            if (matcherHandler.match(item) && deathPack.addItem(item)) {
                itemsAdded += 1
                return@removeAll true
            }

            return@removeAll false
        }

        if (itemsAdded > 0) {
            killer.sendConfigMessage("DEATHPACKS_ITEMS_ADDED", Set("{NUM_OF_ITEMS}", itemsAdded))
        }
    }
}