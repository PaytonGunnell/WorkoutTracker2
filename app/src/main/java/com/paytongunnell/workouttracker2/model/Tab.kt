package com.paytongunnell.workouttracker2.model

import androidx.compose.runtime.Composable

data class Tab(
    val title: String,
    val content: @Composable (() -> Unit)? = null,
    var initialized: Boolean = false,
)
