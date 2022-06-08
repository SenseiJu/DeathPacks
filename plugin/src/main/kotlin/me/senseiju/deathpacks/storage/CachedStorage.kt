package me.senseiju.deathpacks.storage

import me.senseiju.deathpacks.DeathPackImpl
import me.senseiju.deathpacks.DeathPacks
import java.util.*

abstract class CachedStorage(
    plugin: DeathPacks
) : Storage, PlayerCache(plugin) {

    fun getDeathPack(uuid: UUID): DeathPackImpl? {
        return players[uuid]
    }
}