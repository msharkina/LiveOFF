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
import com.lefesafety.liveoff.ui.screen.cards.CardCategoriesScreen
import com.lefesafety.liveoff.ui.screen.cards.CardDetailScreen
import com.lefesafety.liveoff.ui.screen.cards.CardListScreen
import com.lefesafety.liveoff.ui.screen.checklists.ChecklistCategoriesScreen
import com.lefesafety.liveoff.ui.screen.checklists.ChecklistDetailScreen
import com.lefesafety.liveoff.ui.screen.checklists.ChecklistListScreen
import com.lefesafety.liveoff.ui.screen.home.HomeScreen
import com.lefesafety.liveoff.ui.screen.favorites.FavoritesScreen
import com.lefesafety.liveoff.ui.screen.morse.MorseAlphabetScreen
import com.lefesafety.liveoff.ui.screen.morse.MorseTrainerScreen
import com.lefesafety.liveoff.ui.screen.morse.MorseTransmitScreen
import com.lefesafety.liveoff.ui.screen.search.SearchScreen
import com.lefesafety.liveoff.ui.screen.settings.SettingsScreen
import com.lefesafety.liveoff.ui.screen.sos.SosScreen

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
        composable<Screen.Sos> {
            SosScreen(
                onNavigateToEvacuation = { navController.navigate(Screen.ChecklistList("evacuation")) },
                onNavigateToFirstAid = { navController.navigate(Screen.CardList("firstaid")) },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.ChecklistCategories> {
            ChecklistCategoriesScreen(
                onNavigateToChecklistList = { categoryId ->
                    navController.navigate(Screen.ChecklistList(categoryId))
                }
            )
        }
        composable<Screen.ChecklistList> {
            ChecklistListScreen(
                onNavigateToChecklistDetail = { checklistId ->
                    navController.navigate(Screen.ChecklistDetail(checklistId))
                }
            )
        }
        composable<Screen.ChecklistDetail> {
            ChecklistDetailScreen()
        }
        composable<Screen.CardCategories> {
            CardCategoriesScreen(
                onNavigateToCardList = { categoryId ->
                    navController.navigate(Screen.CardList(categoryId))
                }
            )
        }
        composable<Screen.CardList> {
            CardListScreen(
                onNavigateToCardDetail = { cardId ->
                    navController.navigate(Screen.CardDetail(cardId))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.CardDetail> {
            CardDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Search> {
            SearchScreen(onNavigateToCardDetail = { cardId -> navController.navigate(Screen.CardDetail(cardId)) })
        }
        composable<Screen.Favorites> {
            FavoritesScreen(
                onNavigateToCard = { cardId -> navController.navigate(Screen.CardDetail(cardId)) },
                onNavigateToChecklist = { checklistId -> navController.navigate(Screen.ChecklistDetail(checklistId)) }
            )
        }
        composable<Screen.Settings> { SettingsScreen() }
        composable<Screen.MorseAlphabet> {
            MorseAlphabetScreen(
                onNavigateToTrainer = { navController.navigate(Screen.MorseTrainer) },
                onNavigateToTransmit = { navController.navigate(Screen.MorseTransmit) }
            )
        }
        composable<Screen.MorseTrainer> { MorseTrainerScreen() }
        composable<Screen.MorseTransmit> { MorseTransmitScreen() }
    }
}

@Composable
private fun Placeholder(name: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}
