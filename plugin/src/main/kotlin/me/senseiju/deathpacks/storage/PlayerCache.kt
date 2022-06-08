package me.senseiju.deathpacks.storage

import me.senseiju.deathpacks.DeathPackImpl
import me.senseiju.deathpacks.DeathPacks
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.TimeUnit

open class PlayerCache(private val plugin: DeathPacks) : BukkitRunnable() {
    val players = hashMapOf<UUID, DeathPackImpl>()

    private val interval = TimeUnit.MINUTES.toSeconds(2) * 20

    init {
        this.runTaskTimerAsynchronously(plugin, interval, interval)
    }

    override fun run() {
        save()
        cleanup()
    }

    override fun cancel() {
        super.cancel()

        save()
    }

    private fun save() {
        players.keys.forEach { uuid ->
            plugin.storage.saveDeathPack(uuid)
        }
    }

    private fun cleanup() {
        players.keys.filterNot { uuid ->
            Bukkit.getOfflinePlayer(uuid).isOnline
        }.forEach {
            players.remove(it)
        }
    }
}