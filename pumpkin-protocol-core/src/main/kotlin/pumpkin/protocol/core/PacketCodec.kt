package pumpkin.protocol.core

import io.netty.buffer.ByteBuf

interface PacketCodec<P : Packet> {

    fun read(buf: ByteBuf): P

    fun write(buf: ByteBuf, packet: P)
}