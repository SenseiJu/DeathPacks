package me.senseiju.deathpacks.api

import org.bukkit.inventory.ItemStack

interface DeathPack {

    fun isFull(): Boolean

    /**
     * Adds item to the death pack
     *
     * @param item the [ItemStack] to add
     * @return true if added, false if pack is full
     */
    fun addItem(item: ItemStack): Boolean

    fun clearItems(): List<ItemStack>
}