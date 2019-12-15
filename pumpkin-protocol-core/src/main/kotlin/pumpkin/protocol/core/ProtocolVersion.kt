package pumpkin.protocol.core

interface ProtocolVersion : Comparable<ProtocolVersion> {

    val name: String

    val isSupported: Boolean
}