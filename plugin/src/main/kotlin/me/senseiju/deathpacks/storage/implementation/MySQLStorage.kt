package me.senseiju.deathpacks.storage.implementation

import kotlinx.coroutines.runBlocking
import me.senseiju.deathpacks.DeathPackImpl
import me.senseiju.deathpacks.DeathPacks
import me.senseiju.deathpacks.extensions.deserializeToItemStack
import me.senseiju.deathpacks.extensions.serializeToString
import me.senseiju.deathpacks.storage.CachedStorage
import me.senseiju.sentils.storage.Database
import org.bukkit.inventory.ItemStack
import java.util.*

private const val CREATE_PLAYER_DATA_TABLE_QUERY =
    "CREATE TABLE IF NOT EXISTS `player_data`(" +
            "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
            "`serialized_items` TEXT," +
            "`enabled` BOOLEAN);"

private const val SELECT_ALL_PLAYER_DATA_QUERY = "SELECT * FROM `player_data` WHERE `uuid`=?;"
private const val INSERT_OR_UPDATE_ALL_PLAYER_DATA_QUERY =
    "INSERT INTO `player_data`(`uuid`, `serialized_items`, `enabled`) VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE `serialized_items`=?, `enabled`=?;"

class MySQLStorage(plugin: DeathPacks) : CachedStorage(plugin) {
    private val database = Database(plugin.databaseConfig)

    init {
        runBlocking {
            createTables()
        }
    }

    override suspend fun loadDeathPack(uuid: UUID): DeathPackImpl {
        val set = database.asyncQuery(SELECT_ALL_PLAYER_DATA_QUERY, uuid.toString())
        val deathpack = if (set.size() == 0) {
            DeathPackImpl.new()
        } else {
            set.next()

            DeathPackImpl(
                set.getString("serialized_items").deserializeItems(),
                set.getBoolean("enabled")
            )
        }

        return deathpack
    }

    override suspend fun saveDeathPack(uuid: UUID) {
        val deathPack = players[uuid] ?: return

        database.asyncUpdateQuery(
            INSERT_OR_UPDATE_ALL_PLAYER_DATA_QUERY,
            uuid.toString(),
            deathPack.getItems().serializeItems(),
            deathPack.enabled,
            deathPack.getItems().serializeItems(),
            deathPack.enabled
        )
    }

    private suspend fun createTables() {
        database.asyncUpdateQuery(CREATE_PLAYER_DATA_TABLE_QUERY)
    }

    private fun List<ItemStack>.serializeItems(): String {
        return joinToString(",") { it.serializeToString() }
    }

    private fun String.deserializeItems(): ArrayList<ItemStack> {
        if (isEmpty()) {
            return arrayListOf()
        }

        return split(",")
            .mapTo(arrayListOf()) { it.deserializeToItemStack() }
    }

}