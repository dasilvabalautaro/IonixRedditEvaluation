package com.globalhiddenodds.ionixredditevaluation.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.globalhiddenodds.ionixredditevaluation.ui.components.IonixRedditTabRow
import com.globalhiddenodds.ionixredditevaluation.ui.screens.BoardingBody
import com.globalhiddenodds.ionixredditevaluation.ui.screens.PostingsBody
import com.globalhiddenodds.ionixredditevaluation.ui.screens.SearchBody
import com.globalhiddenodds.ionixredditevaluation.ui.values.IonixRedditTheme
import com.globalhiddenodds.ionixredditevaluation.ui.viewmodel.NewsPostingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IonixRedditApp()
        }
    }
}

@Composable
fun IonixRedditApp(viewModel: NewsPostingsViewModel = hiltViewModel()) {
    IonixRedditTheme {
        val allScreens = IonixRedditScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = IonixRedditScreen.fromRoute(backstackEntry.value?.destination?.route)

        Scaffold(topBar = {
            IonixRedditTabRow(
                allScreens = allScreens,
                onTabSelected = {
                    navController.navigate(it.name)
                },
                currentScreen = currentScreen
            )
        }) {
            IonixRedditNavHost(
                navController,
                modifier = Modifier.padding(it), viewModel
            )
        }
    }
}

@Composable
fun IonixRedditNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: NewsPostingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = IonixRedditScreen.Boarding.name,
        modifier = modifier
    ) {
        composable(IonixRedditScreen.Boarding.name) {
            BoardingBody(viewModel)
        }
        composable(IonixRedditScreen.Postings.name) {
            PostingsBody()
        }
        composable(IonixRedditScreen.Search.name) {
            SearchBody()
        }
        composable(IonixRedditScreen.Close.name) {
            android.os.Process.SIGNAL_KILL
            exitProcess(1)
        }
    }
}
