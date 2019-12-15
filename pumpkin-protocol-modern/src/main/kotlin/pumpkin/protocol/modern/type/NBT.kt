package pumpkin.protocol.modern.type

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import pumpkin.nbt.NBTCompound
import pumpkin.nbt.NBTTag
import pumpkin.nbt.readNBT
import pumpkin.nbt.readNBTCompound
import pumpkin.nbt.readNamedNBT
import pumpkin.nbt.readNamedNBTCompound
import pumpkin.nbt.writeNBT
import pumpkin.nbt.writeNamedNBT
import java.io.DataInputStream
import java.io.DataOutputStream

fun ByteBuf.readNBT(): NBTTag =
    DataInputStream(ByteBufInputStream(this)).readNBT()

fun ByteBuf.readNamedNBT(): Pair<String?, NBTTag> =
    DataInputStream(ByteBufInputStream(this)).readNamedNBT()

fun ByteBuf.readNBTCompound(): NBTCompound =
    DataInputStream(ByteBufInputStream(this)).readNBTCompound()

fun ByteBuf.readNamedNBTCompound(): Pair<String?, NBTCompound> =
    DataInputStream(ByteBufInputStream(this)).readNamedNBTCompound()

fun ByteBuf.writeNBT(tag: NBTTag): ByteBuf {
    DataOutputStream(ByteBufOutputStream(this)).writeNBT(tag)
    return this
}

fun ByteBuf.writeNBT(name: String, tag: NBTTag): ByteBuf {
    DataOutputStream(ByteBufOutputStream(this)).writeNamedNBT(name, tag)
    return this
}