package com.lefesafety.liveoff.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable data object Home : Screen
    @Serializable data object Sos : Screen
    @Serializable data object ChecklistCategories : Screen
    @Serializable data class ChecklistList(val categoryId: String) : Screen
    @Serializable data class ChecklistDetail(val checklistId: String) : Screen
    @Serializable data object CardCategories : Screen
    @Serializable data class CardList(val categoryId: String) : Screen
    @Serializable data class CardDetail(val cardId: String) : Screen
    @Serializable data object Search : Screen
    @Serializable data object Favorites : Screen
    @Serializable data object Settings : Screen
    @Serializable data object MorseAlphabet : Screen
    @Serializable data object MorseTrainer : Screen
    @Serializable data object MorseTransmit : Screen
}
