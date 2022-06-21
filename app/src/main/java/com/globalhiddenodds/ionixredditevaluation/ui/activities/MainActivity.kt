package com.globalhiddenodds.ionixredditevaluation.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.globalhiddenodds.ionixredditevaluation.ui.values.IonixRedditTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IonixRedditTheme {
                IonixRedditApp()
            }
        }
    }
}
