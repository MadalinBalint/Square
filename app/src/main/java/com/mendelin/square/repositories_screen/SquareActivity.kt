package com.mendelin.square.repositories_screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.mendelin.square.ui.theme.SquareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SquareActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SquareTheme {
                Surface {
                    SquareScreen()
                }
            }
        }
    }
}
