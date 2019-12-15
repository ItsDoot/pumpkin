package pumpkin.nbt.serialization

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialKind
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.LinkedHashMapSerializer
import kotlinx.serialization.internal.NamedMapClassDescriptor
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.internal.StringSerializer
import pumpkin.nbt.NBTCompound
import pumpkin.nbt.NBTTag
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Serializer(forClass = NBTTag::class)
object NBTTagSerializer : KSerializer<NBTTag> {
    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("NBTTagSerializer") {
        override val kind: SerialKind get() = PolymorphicKind.SEALED
    }

    override fun serialize(encoder: Encoder, obj: NBTTag) {
        verify(encoder)
        TODO()
    }

    override fun deserialize(decoder: Decoder): NBTTag {
        verify(decoder)
        return decoder.decodeNBT()
    }
}

@Serializer(forClass = NBTCompound::class)
object NBTCompoundSerializer : KSerializer<NBTCompound> {
    override val descriptor: SerialDescriptor =
        NamedMapClassDescriptor("NBTCompound", StringSerializer.descriptor, NBTTagSerializer.descriptor)

    override fun serialize(encoder: Encoder, obj: NBTCompound) {
        verify(encoder)
        LinkedHashMapSerializer(StringSerializer, NBTTagSerializer).serialize(encoder, obj.values)
    }

    override fun deserialize(decoder: Decoder): NBTCompound {
        verify(decoder)
        return NBTCompound(LinkedHashMapSerializer(StringSerializer, NBTTagSerializer).deserialize(decoder).toMutableMap())
    }
}

@UseExperimental(ExperimentalContracts::class)
private fun verify(encoder: Encoder) {
    contract {
        returns() implies (encoder is NBTOutput)
    }
    if (encoder !is NBTOutput) error("NBT tag serializer can only be used by NBT format")
}

@UseExperimental(ExperimentalContracts::class)
private fun verify(decoder: Decoder) {
    contract {
        returns() implies (decoder is NBTInput)
    }
    if (decoder !is NBTInput) error("NBT tag serializer can only be used by NBT format")
}