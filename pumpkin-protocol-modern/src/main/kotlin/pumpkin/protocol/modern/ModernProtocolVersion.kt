package pumpkin.protocol.modern

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import pumpkin.protocol.core.ProtocolVersion

sealed class ModernProtocolVersion(
    val protocol: Int,
    override val name: String,
    override val isSupported: Boolean = true
) : ProtocolVersion {

    object Legacy : ModernProtocolVersion(-2, "Legacy", isSupported = false)
    object Unknown : ModernProtocolVersion(-1, "Unknown", isSupported = false)

    object Minecraft_1_7_2 : ModernProtocolVersion(2, "1.7.2", isSupported = false)
    object Minecraft_1_7_6 : ModernProtocolVersion(5, "1.7.6", isSupported = false)
    object Minecraft_1_8 : ModernProtocolVersion(47, "1.8", isSupported = false)
    object Minecraft_1_9 : ModernProtocolVersion(107, "1.9", isSupported = false)
    object Minecraft_1_10_2 : ModernProtocolVersion(210, "1.10.2", isSupported = false)
    object Minecraft_1_11_2 : ModernProtocolVersion(315, "1.11.2", isSupported = false)
    object Minecraft_1_12_2 : ModernProtocolVersion(340, "1.12.2", isSupported = false)
    object Minecraft_1_13_2 : ModernProtocolVersion(404, "1.13.2", isSupported = false)

    object Minecraft_1_14_4 : ModernProtocolVersion(498, "1.14.4")

//    object Minecraft_1_15 : ModernProtocolVersion(573, "1.15")

    override fun compareTo(other: ProtocolVersion): Int {
        require(other is ModernProtocolVersion) { "${other::class.java.name} is not comparable to ModernMinecraftVersion" }
        return this.protocol.compareTo(other.protocol)
    }

    override fun toString(): String = name

    companion object {
        private val protocolMap =
            ModernProtocolVersion::class.sealedSubclasses
                .map { requireNotNull(it.objectInstance) { "$it must be an object" } }
                .sorted()
                .associateBy { it.protocol }
        private val nameMap =
            ModernProtocolVersion::class.sealedSubclasses
                .map { requireNotNull(it.objectInstance) { "$it must be an object" } }
                .sorted()
                .associateBy { it.name }

        operator fun get(protocol: Int): ModernProtocolVersion = protocolMap[protocol] ?: Unknown

        operator fun get(name: String): ModernProtocolVersion = nameMap[name] ?: Unknown

        @JvmField
        val ALL: Collection<ModernProtocolVersion> = protocolMap.values

        @JvmField
        val SUPPORTED: Collection<ModernProtocolVersion> = ALL.filter { it.isSupported }

        @JvmField
        val MINIMUM: ModernProtocolVersion = ALL.first { it.isSupported }

        @JvmField
        val MAXIMUM: ModernProtocolVersion = ALL.last()
    }

    @Serializer(ModernProtocolVersion::class)
    object StringSerializer : KSerializer<ModernProtocolVersion> {
        @UseExperimental(InternalSerializationApi::class)
        override val descriptor: SerialDescriptor = StringDescriptor.withName("ProtocolVersion")

        override fun deserialize(decoder: Decoder): ModernProtocolVersion = ModernProtocolVersion[decoder.decodeString()]

        override fun serialize(encoder: Encoder, obj: ModernProtocolVersion) {
            encoder.encodeString(obj.name)
        }
    }
}