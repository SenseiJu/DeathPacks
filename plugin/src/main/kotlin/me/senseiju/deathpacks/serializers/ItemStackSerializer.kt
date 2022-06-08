package me.senseiju.deathpacks.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.senseiju.deathpacks.extensions.deserializeToItemStack
import me.senseiju.deathpacks.extensions.serializeToString
import org.bukkit.inventory.ItemStack

object ItemStackSerializer : KSerializer<ItemStack> {

    override val descriptor = PrimitiveSerialDescriptor("ItemStack", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ItemStack) {
        encoder.encodeString(value.serializeToString())
    }

    override fun deserialize(decoder: Decoder): ItemStack {
        return decoder.decodeString().deserializeToItemStack()
    }
}