package com.paytongunnell.workouttracker2.utils

import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@NavTypeSerializer
class FirebaseUserSerializer: DestinationsNavTypeSerializer<FirebaseUser> {
    override fun toRouteString(value: FirebaseUser): String {
        return Json.encodeToString(value)
    }

    override fun fromRouteString(routeStr: String): FirebaseUser {
        return Json.decodeFromString(routeStr)
    }
}