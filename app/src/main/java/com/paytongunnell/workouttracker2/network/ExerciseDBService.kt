package com.paytongunnell.workouttracker2.network

import com.paytongunnell.workouttracker2.model.BodyPart
import com.paytongunnell.workouttracker2.model.Exercise
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

object ExerciseDBService {

    private const val BASE_URL = "https://exercisedb.p.rapidapi.com"
    val urlHeaders = mapOf(
        "X-RapidAPI-Key" to "291db17ca4msh79208699702447ap1e6cc2jsn16ff8e292e69",
        "X-RapidAPI-Host" to "exercisedb.p.rapidapi.com"
    )

    val client: HttpClient
        get() = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

    suspend fun getBodyPartExercises(bodyPart: BodyPart, limit: Int, offset: Int): List<Exercise> = client
        .get("$BASE_URL/exercises/bodyPart/${bodyPart.stringValue.lowercase()}?limit=$limit&offset=$offset") { urlHeaders.forEach { (key, value) ->
            header(key, value)
        } }
        .body()

    // will get the first x exercises for each body part
    suspend fun getExercises(limit: Int = 10, offset: Int = 0): List<Exercise> {
        val exercises = BodyPart.values().map { bodyPart ->
            coroutineScope { getBodyPartExercises(bodyPart, limit, offset) }
        }

        return exercises.flatten()
    }
}