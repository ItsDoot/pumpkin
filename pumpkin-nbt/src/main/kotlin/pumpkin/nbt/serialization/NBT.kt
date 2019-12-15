package pumpkin.nbt.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule
import pumpkin.nbt.NBTTag
import pumpkin.nbt.serialization.internal.readNBT

internal const val PRIMITIVE_TAG = "primitive"

/**
 * The main entry point to work with Named Binary Tag (NBT) serialization.
 *
 * Based on https://github.com/natanfudge/Fabric-Drawer
 */
class NBT(val updateMode: UpdateMode = UpdateMode.BANNED, context: SerialModule = EmptyModule) :
    AbstractSerialFormat(context) {

    fun <T> readNBT(deserializer: DeserializationStrategy<T>, nbt: NBTTag): T =
        readNBT(nbt, deserializer)

    companion object : SerialFormat {
        val plain = NBT()

        override val context: SerialModule get() = plain.context

        fun <T> readNBT(deserializer: DeserializationStrategy<T>, nbt: NBTTag): T =
            plain.readNBT(nbt, deserializer)

        override fun install(module: SerialModule) =
            throw IllegalStateException("Do not install anything to the global instance")
    }
}