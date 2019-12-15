package pumpkin.util.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import pumpkin.nbt.serialization.NBT

object Formats {

    val json = Json.plain

    val jsonNoDefaults = Json(JsonConfiguration(encodeDefaults = false))

    val nbt = NBT.plain
}