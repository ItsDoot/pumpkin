package pumpkin.util.json

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import io.vertx.core.json.JsonObject as VertxJsonObject
import io.vertx.core.json.JsonArray as VertxJsonArray

import kotlinx.serialization.json.JsonObject as KotlinxJsonObject
import kotlinx.serialization.json.JsonArray as KotlinxJsonArray

fun Any?.toKotlinx(): JsonElement = when (this) {
    is VertxJsonObject -> this.toKotlinx()
    is VertxJsonArray -> this.toKotlinx()
    null -> JsonNull
    else -> JsonLiteral(this.toString())
}

fun VertxJsonObject.toKotlinx(): KotlinxJsonObject {
    val content = HashMap<String, JsonElement>()
    for ((key, value) in this) {
        content[key] = value.toKotlinx()
    }
    return KotlinxJsonObject(content)
}

fun VertxJsonArray.toKotlinx(): KotlinxJsonArray {
    val content = ArrayList<JsonElement>()
    for (value in this) {
        content += value.toKotlinx()
    }
    return KotlinxJsonArray(content)
}

fun JsonElement.toVertx(): Any? = when (this) {
    is KotlinxJsonObject -> this.toVertx()
    is KotlinxJsonArray -> this.toVertx()
    JsonNull -> null
    is JsonPrimitive ->
        this.intOrNull ?: this.longOrNull ?: this.floatOrNull ?: this.doubleOrNull ?: this.booleanOrNull ?: this.content
}

fun KotlinxJsonObject.toVertx(): VertxJsonObject {
    val content = HashMap<String, Any?>()
    for ((key, value) in this) {
        content[key] = value.toVertx()
    }
    return VertxJsonObject(content)
}

fun KotlinxJsonArray.toVertx(): VertxJsonArray {
    val content = ArrayList<Any?>()
    for (value in this) {
        content += value.toVertx()
    }
    return VertxJsonArray(content)
}