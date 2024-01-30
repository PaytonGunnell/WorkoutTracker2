package com.paytongunnell.workouttracker2.screens.auth.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
        .background(
            color = MaterialTheme.colorScheme.onSurface,
            shape = MaterialTheme.shapes.large
        )
        .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Text(
                text,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp)
            )
        }
    }
}