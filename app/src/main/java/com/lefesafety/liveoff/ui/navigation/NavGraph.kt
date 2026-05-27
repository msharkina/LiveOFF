package com.lefesafety.liveoff.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lefesafety.liveoff.ui.screen.home.HomeScreen

@Composable
fun LiveOffNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToSos = { navController.navigate(Screen.Sos) },
                onNavigateToChecklists = { navController.navigate(Screen.ChecklistCategories) },
                onNavigateToCards = { navController.navigate(Screen.CardCategories) },
                onNavigateToSearch = { navController.navigate(Screen.Search) },
                onNavigateToFavorites = { navController.navigate(Screen.Favorites) },
                onNavigateToMorse = { navController.navigate(Screen.MorseAlphabet) },
                onNavigateToSettings = { navController.navigate(Screen.Settings) }
            )
        }
        composable<Screen.Sos> { Placeholder("SOS") }
        composable<Screen.ChecklistCategories> { Placeholder("Checklist Categories") }
        composable<Screen.ChecklistList> { entry ->
            val route = entry.toRoute<Screen.ChecklistList>()
            Placeholder("Checklists: ${route.categoryId}")
        }
        composable<Screen.ChecklistDetail> { entry ->
            val route = entry.toRoute<Screen.ChecklistDetail>()
            Placeholder("Checklist: ${route.checklistId}")
        }
        composable<Screen.CardCategories> { Placeholder("Card Categories") }
        composable<Screen.CardList> { entry ->
            val route = entry.toRoute<Screen.CardList>()
            Placeholder("Cards: ${route.categoryId}")
        }
        composable<Screen.CardDetail> { entry ->
            val route = entry.toRoute<Screen.CardDetail>()
            Placeholder("Card: ${route.cardId}")
        }
        composable<Screen.Search> { Placeholder("Search") }
        composable<Screen.Favorites> { Placeholder("Favorites") }
        composable<Screen.Settings> { Placeholder("Settings") }
        composable<Screen.MorseAlphabet> { Placeholder("Morse Alphabet") }
        composable<Screen.MorseTrainer> { Placeholder("Morse Trainer") }
        composable<Screen.MorseTransmit> { Placeholder("Morse Transmit") }
    }
}

@Composable
private fun Placeholder(name: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}
