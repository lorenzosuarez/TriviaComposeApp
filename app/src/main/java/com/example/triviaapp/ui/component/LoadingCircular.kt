package com.example.triviaapp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.ui.theme.TriviaAppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadingCircular(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        val (
            loadingCircular,
        ) = createRefs()
        CircularProgressIndicator(
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingCircularPreview() {
    TriviaAppTheme() {
        LoadingCircular()
    }
}