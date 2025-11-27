package com.maestro.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.maestro.demo.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Content(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RectangleShape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "This is your app",
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    MyApplicationTheme {
        Content()
    }
}