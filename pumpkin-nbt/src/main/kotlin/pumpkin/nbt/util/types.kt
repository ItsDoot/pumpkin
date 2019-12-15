package pumpkin.nbt.util

internal val Any?.nullableTypeName: String get() = if (this == null) "<unknown>" else this::class.java.name ?: "<unknown>"