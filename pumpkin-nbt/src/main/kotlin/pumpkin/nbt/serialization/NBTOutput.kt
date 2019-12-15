package pumpkin.nbt.serialization

import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.Encoder
import pumpkin.nbt.NBTTag

interface NBTOutput : Encoder, CompositeEncoder {

    val nbt: NBT

    fun encodeNBT(tag: NBTTag)
}