package pumpkin.nbt.serialization.internal

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.AbstractPolymorphicSerializer
import pumpkin.nbt.NBTCompound
import pumpkin.nbt.serialization.NBTInput

@UseExperimental(InternalSerializationApi::class)
internal fun <T> NBTInput.decodeSerializableValuePolymorphic(deserializer: DeserializationStrategy<T>): T {
    if (deserializer !is AbstractPolymorphicSerializer<*>) {
        return deserializer.deserialize(this)
    }

    val tree = decodeNBT().cast<NBTCompound>()
    val type = tree.getString("type")
    tree.values.remove("type")
    @Suppress("UNCHECKED_CAST")
    val actualSerializer = deserializer.findPolymorphicSerializer(this, type) as KSerializer<T>
    return nbt.readNBT(tree, actualSerializer)
}