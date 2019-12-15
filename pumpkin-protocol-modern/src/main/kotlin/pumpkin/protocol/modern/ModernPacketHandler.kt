package pumpkin.protocol.modern

import pumpkin.protocol.core.PacketHandler
import pumpkin.protocol.modern.packet.handshake.SBHandshakePacket
import pumpkin.protocol.modern.packet.login.CBDisconnectLoginPacket
import pumpkin.protocol.modern.packet.login.CBPluginRequestLoginPacket
import pumpkin.protocol.modern.packet.login.CBSuccessLoginPacket
import pumpkin.protocol.modern.packet.login.SBPluginResponseLoginPacket
import pumpkin.protocol.modern.packet.login.SBStartLoginPacket
import pumpkin.protocol.modern.packet.play.*
import pumpkin.protocol.modern.packet.status.CBResponseStatusPacket
import pumpkin.protocol.modern.packet.status.SBRequestStatusPacket
import pumpkin.protocol.modern.packet.status.TWPingPongStatusPacket

interface ModernPacketHandler : PacketHandler {

    ////////////////////////////////////////////////////
    // Handshake

    fun handle(packet: SBHandshakePacket) {}

    ////////////////////////////////////////////////////
    // Login

    fun handle(packet: CBDisconnectLoginPacket) {}

    fun handle(packet: CBPluginRequestLoginPacket) {}

    fun handle(packet: CBSuccessLoginPacket) {}

    fun handle(packet: SBPluginResponseLoginPacket) {}

    fun handle(packet: SBStartLoginPacket) {}

    ////////////////////////////////////////////////////
    // Play

    fun handle(packet: CBAnimationPlayPacket) {}

    fun handle(packet: CBBlockActionPlayPacket) {}

    fun handle(packet: CBBlockBreakAnimationPlayPacket) {}

    fun handle(packet: CBChatMessagePlayPacket) {}

    fun handle(packet: CBDisconnectPlayPacket) {}

    fun handle(packet: CBHeldItemChangePlayPacket) {}

    fun handle(packet: CBJoinGamePlayPacket) {}

    fun handle(packet: CBPlayerAbilitiesPlayPacket) {}

    fun handle(packet: CBPlayerInfoPlayPacket) {}

    fun handle(packet: CBPlayerPositionAndLookPlayPacket) {}

    fun handle(packet: CBPluginMessagePlayPacket) {}

    fun handle(packet: CBServerDifficultyPlayPacket) {}

    fun handle(packet: CBSpawnExpOrbPlayPacket) {}

    fun handle(packet: CBSpawnPositionPlayPacket) {}

    fun handle(packet: CBTagsPlayPacket) {}

    fun handle(packet: CBUpdateViewPositionPlayPacket) {}

    fun handle(packet: SBAdvancementTabPlayPacket) {}

    fun handle(packet: SBAnimationPlayPacket) {}

    fun handle(packet: SBChatMessagePlayPacket) {}

    fun handle(packet: SBClickWindowPlayPacket) {}

    fun handle(packet: SBClientSettingsPlayPacket) {}

    fun handle(packet: SBClientStatusPlayPacket) {}

    fun handle(packet: SBCloseWindowPlayPacket) {}

    fun handle(packet: SBHeldItemChangePlayPacket) {}

    fun handle(packet: TWKeepAlivePlayPacket) {}

    fun handle(packet: SBPlayerAbilitiesPlayPacket) {}

    fun handle(packet: SBPlayerDiggingPlayPacket) {}

    fun handle(packet: SBPlayerPositionAndLookPlayPacket) {}

    fun handle(packet: SBPluginMessagePlayPacket) {}

    fun handle(packet: SBSetBeaconEffectPlayPacket) {}

    fun handle(packet: SBSetDifficultyPlayPacket) {}

    fun handle(packet: SBTeleportConfirmPlayPacket) {}

    ////////////////////////////////////////////////////
    // Status

    fun handle(packet: CBResponseStatusPacket) {}

    fun handle(packet: SBRequestStatusPacket) {}

    fun handle(packet: TWPingPongStatusPacket) {}
}