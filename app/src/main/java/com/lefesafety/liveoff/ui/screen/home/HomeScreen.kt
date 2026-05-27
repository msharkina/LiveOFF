package com.lefesafety.liveoff.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lefesafety.liveoff.ui.theme.AccentBlue
import com.lefesafety.liveoff.ui.theme.AccentGreen
import com.lefesafety.liveoff.ui.theme.AccentOrange
import com.lefesafety.liveoff.ui.theme.AccentPurple
import com.lefesafety.liveoff.ui.theme.AccentSosRed
import com.lefesafety.liveoff.ui.theme.DarkSurface

private data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    onNavigateToSos: () -> Unit,
    onNavigateToChecklists: () -> Unit,
    onNavigateToCards: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToMorse: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val menuItems = listOf(
        MenuItem(
            title = "SOS",
            icon = Icons.Default.Warning,
            color = AccentSosRed,
            onClick = onNavigateToSos
        ),
        MenuItem(
            title = "Чек-листы",
            icon = Icons.Default.LibraryBooks,
            color = AccentGreen,
            onClick = onNavigateToChecklists
        ),
        MenuItem(
            title = "Карточки",
            icon = Icons.Default.LibraryBooks,
            color = AccentOrange,
            onClick = onNavigateToCards
        ),
        MenuItem(
            title = "Поиск",
            icon = Icons.Default.Search,
            color = AccentBlue,
            onClick = onNavigateToSearch
        ),
        MenuItem(
            title = "Избранное",
            icon = Icons.Default.Favorite,
            color = AccentOrange,
            onClick = onNavigateToFavorites
        ),
        MenuItem(
            title = "Морзе",
            icon = Icons.Default.GraphicEq,
            color = AccentPurple,
            onClick = onNavigateToMorse
        ),
        MenuItem(
            title = "Настройки",
            icon = Icons.Default.Settings,
            color = Color.Gray,
            onClick = onNavigateToSettings
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LiveOFF",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Руководство по выживанию",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(menuItems) { item ->
                HomeMenuCard(item = item)
            }
        }
    }
}

@Composable
private fun HomeMenuCard(item: MenuItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.color,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
