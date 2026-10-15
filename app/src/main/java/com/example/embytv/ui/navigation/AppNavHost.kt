package com.example.embytv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.embytv.data.preferences.EmbyPreferences
import com.example.embytv.ui.LocalServerUrl
import com.example.embytv.ui.home.HomeScreen
import com.example.embytv.ui.login.LoginScreen
import com.example.embytv.ui.detail.DetailScreen
import com.example.embytv.ui.series.SeriesScreen
import com.example.embytv.ui.episodes.EpisodesScreen
import com.example.embytv.ui.player.PlayerScreen
import com.example.embytv.ui.search.SearchScreen
import com.example.embytv.ui.settings.SettingsScreen

object NavGraph {
    const val LOGIN = "login"
    const val HOME = "home"
    const val DETAIL = "detail/{itemId}"
    const val SERIES = "series/{seriesId}"
    const val EPISODES = "episodes/{seasonId}"
    const val PLAYER = "player/{mediaSourceId}"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    
    const val ITEM_ID_ARG = "itemId"
    const val SERIES_ID_ARG = "seriesId"
    const val SEASON_ID_ARG = "seasonId"
    const val MEDIA_SOURCE_ID_ARG = "mediaSourceId"
}

@Composable
fun AppNavHost(
    navController: androidx.navigation.compose.NavHostController = rememberNavController(),
    application: com.example.embytv.EmbyTVApplication? = null
) {
    // Read current server URL from shared preferences.
    val prefs = application?.let { EmbyPreferences(it.applicationContext) }
    val serverUrlFlow = prefs?.getServerUrl()

    var serverUrlValue by androidx.compose.runtime.mutableStateOf("")
    androidx.compose.runtime.LaunchedEffect(serverUrlFlow) {
        serverUrlFlow?.collect { url ->
            serverUrlValue = url ?: ""
        }
    }

    androidx.compose.runtime.CompositionLocalProvider(LocalServerUrl provides serverUrlValue) {
        NavHost(navController = navController, startDestination = NavGraph.LOGIN) {
            composable(NavGraph.LOGIN) {
                val viewModel: com.example.embytv.viewmodel.LoginViewModel = hiltNavGraphViewModel()
                LoginScreen(
                    onLoginSuccess = { navController.navigate(NavGraph.HOME) { popUpTo(NavGraph.LOGIN) { inclusive = true } } },
                    onNavigateToSettings = { navController.navigate(NavGraph.SETTINGS) }
                )
            }

            composable(NavGraph.HOME) {
                val viewModel: com.example.embytv.viewmodel.HomeViewModel = hiltNavGraphViewModel()
                HomeScreen(
                    onItemClick = { itemId -> navController.navigate("${NavGraph.DETAIL}/$itemId") },
                    onSearchClick = { navController.navigate(NavGraph.SEARCH) },
                    onSettingsClick = { navController.navigate(NavGraph.SETTINGS) },
                    onLogout = { navController.navigate(NavGraph.LOGIN) { popUpTo(NavGraph.HOME) { inclusive = true } } }
                )
            }

            composable(NavGraph.DETAIL, arguments = listOf(navArgument(NavGraph.ITEM_ID_ARG) { type = NavType.StringType })) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString(NavGraph.ITEM_ID_ARG) ?: ""
                val viewModel: com.example.embytv.viewmodel.DetailViewModel = hiltNavGraphViewModel()
                DetailScreen(
                    itemId = itemId,
                    onBack = { navController.popBackStack() },
                    onPlayClick = { mediaSourceId ->
                        navController.navigate("${NavGraph.PLAYER}/$mediaSourceId")
                    },
                    onSeriesClick = { seriesId ->
                        navController.navigate("${NavGraph.SERIES}/$seriesId")
                    }
                )
            }

            composable(NavGraph.SERIES, arguments = listOf(navArgument(NavGraph.SERIES_ID_ARG) { type = NavType.StringType })) { backStackEntry ->
                val seriesId = backStackEntry.arguments?.getString(NavGraph.SERIES_ID_ARG) ?: ""
                SeriesScreen(
                    seriesId = seriesId,
                    onSeasonClick = { seasonId -> navController.navigate("${NavGraph.EPISODES}/$seasonId") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(NavGraph.EPISODES, arguments = listOf(navArgument(NavGraph.SEASON_ID_ARG) { type = NavType.StringType })) { backStackEntry ->
                val seasonId = backStackEntry.arguments?.getString(NavGraph.SEASON_ID_ARG) ?: ""
                EpisodesScreen(
                    seasonId = seasonId,
                    onEpisodeClick = { itemId -> navController.navigate("${NavGraph.DETAIL}/$itemId") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(NavGraph.PLAYER, arguments = listOf(navArgument(NavGraph.MEDIA_SOURCE_ID_ARG) { type = NavType.StringType })) { backStackEntry ->
                val mediaSourceId = backStackEntry.arguments?.getString(NavGraph.MEDIA_SOURCE_ID_ARG) ?: ""
                PlayerScreen(
                    mediaSourceId = mediaSourceId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(NavGraph.SEARCH) {
                SearchScreen(
                    onItemClick = { itemId -> navController.navigate("${NavGraph.DETAIL}/$itemId") },
                    onNavigateUp = { navController.popBackStack() }
                )
            }

            composable(NavGraph.SETTINGS) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = { navController.navigate(NavGraph.LOGIN) { popUpTo(NavGraph.HOME) { inclusive = true } } }
                )
            }
        }
    }
}
