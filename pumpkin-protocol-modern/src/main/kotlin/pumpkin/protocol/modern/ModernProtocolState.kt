package pumpkin.protocol.modern

import pumpkin.protocol.core.Direction
import pumpkin.protocol.core.ProtocolRegistry
import pumpkin.protocol.core.ProtocolState
import pumpkin.protocol.modern.ModernProtocolVersion.Minecraft_1_14_4
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

enum class ModernProtocolState : ProtocolState<ModernPacket> {
    HANDSHAKE {
        init {
            serverbound[0x00 to Minecraft_1_14_4] = SBHandshakePacket
        }
    },
    PLAY {
        init {
            serverbound[0x00 to Minecraft_1_14_4] = SBTeleportConfirmPlayPacket
            // serverbound[0x01 to Minecraft_1_14_4] = SBQueryBlockNBTPlayPacket
            serverbound[0x02 to Minecraft_1_14_4] = SBSetDifficultyPlayPacket
            serverbound[0x03 to Minecraft_1_14_4] = SBChatMessagePlayPacket
            serverbound[0x04 to Minecraft_1_14_4] = SBClientStatusPlayPacket
            serverbound[0x05 to Minecraft_1_14_4] = SBClientSettingsPlayPacket
            // serverbound[0x06 to Minecraft_1_14_4] = SBTabCompletePlayPacket
            // serverbound[0x07 to Minecraft_1_14_4] = SBConfirmTransactionPlayPacket
            // serverbound[0x08 to Minecraft_1_14_4] = SBClickWindowButtonPlayPacket
            serverbound[0x09 to Minecraft_1_14_4] = SBClickWindowPlayPacket
            serverbound[0x0A to Minecraft_1_14_4] = SBCloseWindowPlayPacket
            serverbound[0x0B to Minecraft_1_14_4] = SBPluginMessagePlayPacket
            // serverbound[0x0C to Minecraft_1_14_4] = SBEditBookPlayPacket
            // serverbound[0x0D to Minecraft_1_14_4] = SBQueryEntityNBTPlayPacket
            // serverbound[0x0E to Minecraft_1_14_4] = SBUseEntityPlayPacket
            serverbound[0x0F to Minecraft_1_14_4] = TWKeepAlivePlayPacket
            // serverbound[0x10 to Minecraft_1_14_4] = SBLockDifficultyPlayPacket
            // serverbound[0x11 to Minecraft_1_14_4] = SBPlayerPositionPlayPacket
            serverbound[0x12 to Minecraft_1_14_4] = SBPlayerPositionAndLookPlayPacket
            // serverbound[0x13 to Minecraft_1_14_4] = SBPlayerLookPlayPacket
            // serverbound[0x14 to Minecraft_1_14_4] = SBPlayerPlayPacket
            // serverbound[0x15 to Minecraft_1_14_4] = SBVehicleMovePlayPacket
            // serverbound[0x16 to Minecraft_1_14_4] = SBSteerBoatPlayPacket
            // serverbound[0x17 to Minecraft_1_14_4] = SBPickItemPlayPacket
            // serverbound[0x18 to Minecraft_1_14_4] = SBCraftRecipeRequestPlayPacket
            serverbound[0x19 to Minecraft_1_14_4] = SBPlayerAbilitiesPlayPacket
            serverbound[0x1A to Minecraft_1_14_4] = SBPlayerDiggingPlayPacket
            // serverbound[0x1B to Minecraft_1_14_4] = SBEntityActionPlayPacket
            // serverbound[0x1C to Minecraft_1_14_4] = SBSteerVehiclePlayPacket
            // serverbound[0x1D to Minecraft_1_14_4] = SBRecipeBookDataPlayPacket
            // serverbound[0x1E to Minecraft_1_14_4] = SBNameItemPlayPacket
            // serverbound[0x1F to Minecraft_1_14_4] = SBResourcePackStatusPlayPacket
            serverbound[0x20 to Minecraft_1_14_4] = SBAdvancementTabPlayPacket
            // serverbound[0x21 to Minecraft_1_14_4] = SBSelectTradePlayPacket
            serverbound[0x22 to Minecraft_1_14_4] = SBSetBeaconEffectPlayPacket
            serverbound[0x23 to Minecraft_1_14_4] = SBHeldItemChangePlayPacket
            // serverbound[0x24 to Minecraft_1_14_4] = SBUpdateCommandBlockPlayPacket
            // serverbound[0x25 to Minecraft_1_14_4] = SBUpdateCommandBlockMinecartPlayPacket
            // serverbound[0x26 to Minecraft_1_14_4] = SBCreativeInventoryActionPlayPacket
            // serverbound[0x27 to Minecraft_1_14_4] = SBUpdateJigsawBlockPlayPacket
            // serverbound[0x28 to Minecraft_1_14_4] = SBUpdateStructureBlockPlayPacket
            // serverbound[0x29 to Minecraft_1_14_4] = SBUpdateSignPlayPacket
            serverbound[0x2A to Minecraft_1_14_4] = SBAnimationPlayPacket
            // serverbound[0x2B to Minecraft_1_14_4] = SBSpectatePlayPacket
            // serverbound[0x2C to Minecraft_1_14_4] = SBPlayerBlockPlacementPlayPacket
            // serverbound[0x2D to Minecraft_1_14_4] = SBUseItemPlayPacket

            // clientbound[0x00 to Minecraft_1_14_4] = CBSpawnObjectPlayPacket
            clientbound[0x01 to Minecraft_1_14_4] = CBSpawnExpOrbPlayPacket
            // clientbound[0x02 to Minecraft_1_14_4] = CBSpawnGlobalEntityPlayPacket
            // clientbound[0x03 to Minecraft_1_14_4] = CBSpawnMobPlayPacket
            // clientbound[0x04 to Minecraft_1_14_4] = CBSpawnPaintingPlayPacket
            // clientbound[0x05 to Minecraft_1_14_4] = CBSpawnPlayerPlayPacket
            clientbound[0x06 to Minecraft_1_14_4] = CBAnimationPlayPacket
            // clientbound[0x07 to Minecraft_1_14_4] = CBStatisticsPlayPacket
            clientbound[0x08 to Minecraft_1_14_4] = CBBlockBreakAnimationPlayPacket
            // clientbound[0x09 to Minecraft_1_14_4] = CBUpdateBlockEntityPlayPacket
            clientbound[0x0A to Minecraft_1_14_4] = CBBlockActionPlayPacket
            // clientbound[0x0B to Minecraft_1_14_4] = CBBlockChangePlayPacket
            // clientbound[0x0C to Minecraft_1_14_4] = CBBossBarPlayPacket
            clientbound[0x0D to Minecraft_1_14_4] = CBServerDifficultyPlayPacket
            clientbound[0x0E to Minecraft_1_14_4] = CBChatMessagePlayPacket
            // clientbound[0x0F to Minecraft_1_14_4] = CBMultiBlockChangePlayPacket
            // clientbound[0x10 to Minecraft_1_14_4] = CBTabCompletePlayPacket
            // clientbound[0x11 to Minecraft_1_14_4] = CBDeclareCommandsPlayPacket
            // clientbound[0x12 to Minecraft_1_14_4] = CBConfirmTransactionPlayPacket
            // clientbound[0x13 to Minecraft_1_14_4] = CBCloseWindowPlayPacket
            // clientbound[0x14 to Minecraft_1_14_4] = CBWindowItemsPlayPacket
            // clientbound[0x15 to Minecraft_1_14_4] = CBWindowPropertyPlayPacket
            // clientbound[0x16 to Minecraft_1_14_4] = CBSetSlotPlayPacket
            // clientbound[0x17 to Minecraft_1_14_4] = CBSetCooldownPlayPacket
            clientbound[0x18 to Minecraft_1_14_4] = CBPluginMessagePlayPacket
            // clientbound[0x19 to Minecraft_1_14_4] = CBNamedSoundEffectPlayPacket
            clientbound[0x1A to Minecraft_1_14_4] = CBDisconnectPlayPacket
            // clientbound[0x1B to Minecraft_1_14_4] = CBEntityStatusPlayPacket
            // clientbound[0x1C to Minecraft_1_14_4] = CBExplosionPlayPacket
            // clientbound[0x1D to Minecraft_1_14_4] = CBUnloadChunkPlayPacket
            // clientbound[0x1E to Minecraft_1_14_4] = CBChangeGameStatePlayPacket
            // clientbound[0x1F to Minecraft_1_14_4] = CBOpenHorseWindowPlayPacket
            clientbound[0x20 to Minecraft_1_14_4] = TWKeepAlivePlayPacket
            // clientbound[0x21 to Minecraft_1_14_4] = CBChunkDataPlayPacket
            // clientbound[0x22 to Minecraft_1_14_4] = CBEffectPlayPacket
            // clientbound[0x23 to Minecraft_1_14_4] = CBParticlePlayPacket
            // clientbound[0x24 to Minecraft_1_14_4] = CBUpdateLightPlayPacket
            clientbound[0x25 to Minecraft_1_14_4] = CBJoinGamePlayPacket
            // clientbound[0x26 to Minecraft_1_14_4] = CBMapDataPlayPacket
            // clientbound[0x27 to Minecraft_1_14_4] = CBTradeListPlayPacket
            // clientbound[0x28 to Minecraft_1_14_4] = CBEntityRelativeMovePlayPacket
            // clientbound[0x29 to Minecraft_1_14_4] = CBEntityLookAndRelativeMovePlayPacket
            // clientbound[0x2A to Minecraft_1_14_4] = CBEntityLookPlayPacket
            // clientbound[0x2B to Minecraft_1_14_4] = CBEntityPlayPacket
            // clientbound[0x2C to Minecraft_1_14_4] = CBVehicleMovePlayPacket
            // clientbound[0x2D to Minecraft_1_14_4] = CBOpenBookPlayPacket
            // clientbound[0x2E to Minecraft_1_14_4] = CBOpenWindowPlayPacket
            // clientbound[0x2F to Minecraft_1_14_4] = CBOpenSignEditorPlayPacket
            // clientbound[0x30 to Minecraft_1_14_4] = CBCraftRecipeResponsePlayPacket
            clientbound[0x31 to Minecraft_1_14_4] = CBPlayerAbilitiesPlayPacket
            // clientbound[0x32 to Minecraft_1_14_4] = CBCombatEventPlayPacket
            clientbound[0x33 to Minecraft_1_14_4] = CBPlayerInfoPlayPacket
            // clientbound[0x34 to Minecraft_1_14_4] = CBFacePlayerPlayPacket
            clientbound[0x35 to Minecraft_1_14_4] = CBPlayerPositionAndLookPlayPacket
            // clientbound[0x36 to Minecraft_1_14_4] = CBUnlockRecipesPlayPacket
            // clientbound[0x37 to Minecraft_1_14_4] = CBDestroyEntitiesPlayPacket
            // clientbound[0x38 to Minecraft_1_14_4] = CBRemoveEntityEffectPlayPacket
            // clientbound[0x39 to Minecraft_1_14_4] = CBResourcePackSendPlayPacket
            // clientbound[0x3A to Minecraft_1_14_4] = CBRespawnPlayPacket
            // clientbound[0x3B to Minecraft_1_14_4] = CBEntityHeadLookPlayPacket
            // clientbound[0x3C to Minecraft_1_14_4] = CBSelectAdvancementTabPlayPacket
            // clientbound[0x3D to Minecraft_1_14_4] = CBWorldBorderPlayPacket
            // clientbound[0x3E to Minecraft_1_14_4] = CBCameraPlayPacket
            clientbound[0x3F to Minecraft_1_14_4] = CBHeldItemChangePlayPacket
            clientbound[0x40 to Minecraft_1_14_4] = CBUpdateViewPositionPlayPacket
            // clientbound[0x41 to Minecraft_1_14_4] = CBUpdateViewDistancePlayPacket
            // clientbound[0x42 to Minecraft_1_14_4] = CBDisplayScoreboardPlayPacket
            // clientbound[0x43 to Minecraft_1_14_4] = CBEntityMetadataPlayPacket
            // clientbound[0x44 to Minecraft_1_14_4] = CBAttachEntityPlayPacket
            // clientbound[0x45 to Minecraft_1_14_4] = CBEntityVelocityPlayPacket
            // clientbound[0x46 to Minecraft_1_14_4] = CBEntityEquipmentPlayPacket
            // clientbound[0x47 to Minecraft_1_14_4] = CBSetExperiencePlayPacket
            // clientbound[0x48 to Minecraft_1_14_4] = CBUpdateHealthPlayPacket
            // clientbound[0x49 to Minecraft_1_14_4] = CBScoreboardObjectivePlayPacket
            // clientbound[0x4A to Minecraft_1_14_4] = CBSetPassengersPlayPacket
            // clientbound[0x4B to Minecraft_1_14_4] = CBTeamsPlayPacket
            // clientbound[0x4C to Minecraft_1_14_4] = CBUpdateScorePlayPacket
            clientbound[0x4D to Minecraft_1_14_4] = CBSpawnPositionPlayPacket
            // clientbound[0x4E to Minecraft_1_14_4] = CBTimeUpdatePlayPacket
            // clientbound[0x4F to Minecraft_1_14_4] = CBTitlePlayPacket
            // clientbound[0x50 to Minecraft_1_14_4] = CBEntitySoundEffectPlayPacket
            // clientbound[0x51 to Minecraft_1_14_4] = CBSoundEffectPlayPacket
            // clientbound[0x52 to Minecraft_1_14_4] = CBStopSoundPlayPacket
            // clientbound[0x53 to Minecraft_1_14_4] = CBPlayerListHeaderAndFooterPlayPacket
            // clientbound[0x54 to Minecraft_1_14_4] = CBNBTQueryResponsePlayPacket
            // clientbound[0x55 to Minecraft_1_14_4] = CBCollectItemPlayPacket
            // clientbound[0x56 to Minecraft_1_14_4] = CBEntityTeleportPlayPacket
            // clientbound[0x57 to Minecraft_1_14_4] = CBAdvancementsPlayPacket
            // clientbound[0x58 to Minecraft_1_14_4] = CBEntityPropertiesPlayPacket
            // clientbound[0x59 to Minecraft_1_14_4] = CBEntityEffectPlayPacket
            // clientbound[0x5A to Minecraft_1_14_4] = CBDeclareRecipesPlayPacket
            clientbound[0x5B to Minecraft_1_14_4] = CBTagsPlayPacket
            // clientbound[0x5C to Minecraft_1_14_4] = CBAcknowledgePlayerDiggingPlayPacket
        }
    },
    STATUS {
        init {
            serverbound[0x00 to Minecraft_1_14_4] = SBRequestStatusPacket
            serverbound[0x01 to Minecraft_1_14_4] = TWPingPongStatusPacket

            clientbound[0x00 to Minecraft_1_14_4] = CBResponseStatusPacket
            clientbound[0x01 to Minecraft_1_14_4] = TWPingPongStatusPacket
        }
    },
    LOGIN {
        init {
            serverbound[0x00 to Minecraft_1_14_4] = SBStartLoginPacket
            serverbound[0x02 to Minecraft_1_14_4] = SBPluginResponseLoginPacket

            clientbound[0x00 to Minecraft_1_14_4] = CBDisconnectLoginPacket
            clientbound[0x02 to Minecraft_1_14_4] = CBSuccessLoginPacket
            clientbound[0x04 to Minecraft_1_14_4] = CBPluginRequestLoginPacket
        }
    };

    override val clientbound: ProtocolRegistry<ModernPacket> = ProtocolRegistry(Direction.CLIENT_BOUND, ModernProtocolVersion.SUPPORTED)
    override val serverbound: ProtocolRegistry<ModernPacket> = ProtocolRegistry(Direction.SERVER_BOUND, ModernProtocolVersion.SUPPORTED)

    companion object {
        fun fromHandshake(id: Int): ModernProtocolState = when (id) {
            1 -> STATUS
            2 -> LOGIN
            else -> throw IllegalStateException("Invalid next state: $id")
        }
    }
}