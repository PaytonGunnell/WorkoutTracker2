package com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R

@Composable
fun IconTab(
    index: Int,
    selectedTab: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabIcon = arrayOf(
        R.drawable.profile_icon,
        R.drawable.clock_icon,
        R.drawable.plus_icon,
        R.drawable.dumbbell_icon,
        R.drawable.crown_icon
    )
    val tabText = arrayOf("Profile","History","Start Workout","Exercises","Upgrade")

    Box(
        modifier = modifier
            .padding(0.dp)
            .clickable { onClick(index) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = tabIcon[index]),
                contentDescription = null,
                tint = if (index == selectedTab) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .weight(2f)
//                    .aspectRatio(1f)
            )
            Text(
                text = tabText[index],
                fontSize = TextUnit(value = 11f, TextUnitType.Sp),
                textAlign = TextAlign.Center,
                color = if (index == selectedTab) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}