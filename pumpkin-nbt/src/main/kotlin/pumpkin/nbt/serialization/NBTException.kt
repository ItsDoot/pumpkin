package pumpkin.nbt.serialization

import kotlinx.serialization.SerializationException

open class NBTException(message: String) : SerializationException(message)

class NBTDecodingException(position: Int, message: String) : NBTException("Invalid NBT at $position: $message")

class NBTEncodingException(message: String): NBTException(message)