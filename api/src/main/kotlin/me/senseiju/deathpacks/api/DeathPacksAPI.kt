package me.senseiju.deathpacks.api

import java.util.*

interface DeathPacksAPI {

    fun getDeathPack(uuid: UUID): DeathPack?
}