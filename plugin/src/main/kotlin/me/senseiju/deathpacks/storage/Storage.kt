package me.senseiju.deathpacks.storage

import me.senseiju.deathpacks.DeathPackImpl
import java.util.*

interface Storage {

    fun loadDeathPack(uuid: UUID): DeathPackImpl

    fun saveDeathPack(uuid: UUID)
}