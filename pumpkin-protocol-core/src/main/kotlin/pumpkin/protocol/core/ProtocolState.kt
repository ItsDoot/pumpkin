package pumpkin.protocol.core

interface ProtocolState<out P : Packet> {

    val clientbound: ProtocolRegistry<P>

    val serverbound: ProtocolRegistry<P>
}