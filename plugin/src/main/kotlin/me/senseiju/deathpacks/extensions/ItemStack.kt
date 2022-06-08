package me.senseiju.deathpacks.extensions

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

fun ItemStack.serializeToString(): String {
    val outputStream = ByteArrayOutputStream()
    val bukkitOutputStream = BukkitObjectOutputStream(outputStream)

    bukkitOutputStream.writeObject(this)
    bukkitOutputStream.flush()

    return Base64.getEncoder().encodeToString(outputStream.toByteArray())
}

fun String.deserializeToItemStack(): ItemStack {
    val inputStream = ByteArrayInputStream(Base64.getDecoder().decode(this))
    val bukkitInputStream = BukkitObjectInputStream(inputStream)

    return bukkitInputStream.readObject() as ItemStack
}

fun ItemStack.useItemMeta(update: (ItemMeta) -> Unit) {
    val meta = itemMeta
    update(meta)
    itemMeta = meta
}