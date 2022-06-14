package me.senseiju.deathpacks.storage

import me.senseiju.deathpacks.DeathPackImpl
import java.util.*

interface Storage {

    suspend fun loadDeathPack(uuid: UUID): DeathPackImpl

    suspend fun saveDeathPack(uuid: UUID)
}