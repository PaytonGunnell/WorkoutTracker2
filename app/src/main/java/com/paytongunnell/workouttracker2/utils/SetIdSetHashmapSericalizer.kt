package com.paytongunnell.workouttracker2.utils

import androidx.room.TypeConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import com.paytongunnell.workouttracker2.model.WorkoutSet
import kotlinx.serialization.Serializable

@Serializable
data class SerializableHashMap(
    val data: Map<String, WorkoutSet>
)

object SetIdSetHashmapSericalizer : KSerializer<Map<String, WorkoutSet>> {
    private val hashMapSerializer = SerializableHashMap.serializer()

    override val descriptor: SerialDescriptor = hashMapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Map<String, WorkoutSet>) {
        val serializableHashMap = SerializableHashMap(value)
        hashMapSerializer.serialize(encoder, serializableHashMap)
    }

    override fun deserialize(decoder: Decoder): Map<String, WorkoutSet> {
        val serializableHashMap = hashMapSerializer.deserialize(decoder)
        return serializableHashMap.data
    }
}

//object HashMapStringSetTypeConverter {
//
//    private val json = Json { ignoreUnknownKeys = true }
//
//    @TypeConverter
//    @JvmStatic
//    fun stringToMap(value: String): Map<String, WorkoutSet> {
//        return json.decodeFromString(SetIdSetHashmapSericalizer, value)
////        return Gson().fromJson(value,  object : TypeToken<Map<String, Set>>() {}.type)
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun mapToString(value: Map<String, WorkoutSet>?): String {
//        return json.encodeToString(SetIdSetHashmapSericalizer, value)
////        return if(value == null) "" else Gson().toJson(value)
//    }
//}

class HashMapStringSetTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromJson(jsonString: String?): Map<String, WorkoutSet>? {
        if (jsonString == null) {
            return null
        }
        return json.decodeFromString(SetIdSetHashmapSericalizer, jsonString)
    }

    @TypeConverter
    fun toJson(value: Map<String, WorkoutSet>?): String? {
        if (value == null) {
            return null
        }
        return json.encodeToString(SetIdSetHashmapSericalizer, value)
    }
}
