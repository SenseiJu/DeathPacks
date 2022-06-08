package me.senseiju.deathpacks

import me.senseiju.deathpacks.api.DeathPack
import me.senseiju.deathpacks.api.DeathPacksAPI
import me.senseiju.deathpacks.storage.CachedStorage
import java.util.*

class DeathPacksAPIImpl(private val storage: CachedStorage) : DeathPacksAPI {

    override fun getDeathPack(uuid: UUID): DeathPack? {
        return storage.getDeathPack(uuid)
    }
}