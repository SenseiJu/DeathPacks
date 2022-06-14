package me.senseiju.deathpacks.storage.implementation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.senseiju.deathpacks.DeathPackImpl
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.json
import me.senseiju.deathpacks.storage.CachedStorage
import java.io.File
import java.util.*

class JSONStorage(private val plugin: DeathPacks) : CachedStorage(plugin) {

    init {
        File(plugin.dataFolder, "data").mkdirs()
    }

    override suspend fun loadDeathPack(uuid: UUID): DeathPackImpl {
        val file = File(plugin.dataFolder, "data/$uuid.json")
        val deathPack = if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
            }
            DeathPackImpl.new()
        } else {
            json.decodeFromString(file.readText())
        }

        return deathPack
    }

    override suspend fun saveDeathPack(uuid: UUID) {
        val deathPack = players[uuid] ?: return

        val file = File(plugin.dataFolder, "data/$uuid.json")
        if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
            }
        }

        file.writeText(json.encodeToString(deathPack))
    }
}