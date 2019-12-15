package pumpkin.nbt.serialization

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.Decoder
import pumpkin.nbt.NBTTag

interface NBTInput : Decoder, CompositeDecoder {

    val nbt: NBT

    fun decodeNBT(): NBTTag
}