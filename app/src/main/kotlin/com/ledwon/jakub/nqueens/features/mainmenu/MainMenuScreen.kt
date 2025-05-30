package com.ledwon.jakub.nqueens.features.mainmenu

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ledwon.jakub.nqueens.ui.theme.NQueensTheme

@Composable
fun MainMenuScreen() {
    Scaffold {
        Text(
            modifier = Modifier.padding(it),
            text = "Hello World"
        )
    }
}

@Preview
@Composable
private fun MainMenuScreenPreview() {
    NQueensTheme {
        MainMenuScreen()
    }
}
