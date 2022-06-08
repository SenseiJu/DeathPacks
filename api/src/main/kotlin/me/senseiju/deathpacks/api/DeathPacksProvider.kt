package me.senseiju.deathpacks.api

class DeathPacksProvider {
    companion object {
        private lateinit var api: DeathPacksAPI

        @JvmStatic
        fun get(): DeathPacksAPI {
            return api
        }

        @JvmStatic
        fun set(api: DeathPacksAPI) {
            DeathPacksProvider.api = api
        }
    }
}