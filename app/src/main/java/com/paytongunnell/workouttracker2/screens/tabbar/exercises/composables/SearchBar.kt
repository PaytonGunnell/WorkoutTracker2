package com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = ""
) {

    BasicTextField(
        value = value,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
        onValueChange = onValueChange,
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions( imeAction = ImeAction.Search ),
        keyboardActions = KeyboardActions { onSearch(value) },
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search_icon))
                Spacer(modifier = Modifier.width(10.dp))
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            modifier = Modifier.alpha(0.5f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                }
            }
        },
    )
}