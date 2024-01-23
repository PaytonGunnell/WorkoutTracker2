package com.paytongunnell.workouttracker2.utils

import androidx.room.TypeConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray

object StringListSerializer : KSerializer<List<String>> {
    private val listSerializer = ListSerializer(String.serializer())
    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<String>) {
        listSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): List<String> = with(decoder as JsonDecoder) {
        decodeJsonElement().jsonArray.mapNotNull {
            try {
                json.decodeFromJsonElement(String.serializer(), it)
            } catch (e: SerializationException) {
                e.printStackTrace()
                null
            }
        }
    }
}

class StringListTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromJson(jsonString: String?): List<String>? {
        if (jsonString == null) {
            return null
        }
        return json.decodeFromString(StringListSerializer, jsonString)
    }

    @TypeConverter
    fun toJson(value: List<String>?): String? {
        if (value == null) {
            return null
        }
        return json.encodeToString(StringListSerializer, value)
    }
}
