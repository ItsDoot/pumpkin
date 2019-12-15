package pumpkin.protocol.modern

import pumpkin.protocol.core.Packet

sealed class ModernPacket : Packet {

    abstract fun handle(handler: ModernPacketHandler)

    abstract class Handshake : ModernPacket()

    abstract class Login : ModernPacket()

    abstract class Play : ModernPacket()

    abstract class Status : ModernPacket()
}