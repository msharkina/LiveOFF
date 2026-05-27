package com.lefesafety.liveoff.ui.screen.search

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Category
import com.lefesafety.liveoff.domain.model.Difficulty
import com.lefesafety.liveoff.ui.theme.AccentBlue
import com.lefesafety.liveoff.ui.theme.AccentGreen
import com.lefesafety.liveoff.ui.theme.AccentOrange
import com.lefesafety.liveoff.ui.theme.AccentRed
import com.lefesafety.liveoff.ui.theme.DarkBackground
import com.lefesafety.liveoff.ui.theme.DarkSurface
import com.lefesafety.liveoff.ui.theme.DarkSurfaceVariant
import com.lefesafety.liveoff.ui.theme.TextPrimary
import com.lefesafety.liveoff.ui.theme.TextSecondary

@Composable
fun SearchScreen(
    onNavigateToCardDetail: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val results by viewModel.filteredResults.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            placeholder = {
                Text(
                    text = "Поиск...",
                    color = TextSecondary
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = AccentBlue
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = DarkSurfaceVariant,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = AccentBlue,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            )
        )

        if (categories.isNotEmpty()) {
            CategoryFilterRow(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = viewModel::onCategorySelected,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        when {
            query.isBlank() -> {
                EmptyState(message = "Введите запрос")
            }
            results.isEmpty() -> {
                EmptyState(message = "Ничего не найдено")
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(results, key = { it.id }) { card ->
                        val categoryName = categories.find { it.id == card.categoryId }?.name ?: ""
                        SearchResultItem(
                            card = card,
                            categoryName = categoryName,
                            onClick = { onNavigateToCardDetail(card.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategoryId == null,
            onClick = { onCategorySelected(null) },
            label = { Text("Все") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = AccentBlue,
                selectedLabelColor = TextPrimary,
                containerColor = DarkSurface,
                labelColor = TextSecondary
            )
        )
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategoryId == category.id,
                onClick = {
                    onCategorySelected(
                        if (selectedCategoryId == category.id) null else category.id
                    )
                },
                label = { Text(category.name) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentBlue,
                    selectedLabelColor = TextPrimary,
                    containerColor = DarkSurface,
                    labelColor = TextSecondary
                )
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    card: Card,
    categoryName: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = DarkSurface,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                if (categoryName.isNotBlank()) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
            DifficultyBadge(difficulty = card.difficulty)
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: Difficulty) {
    val (label, color) = when (difficulty) {
        Difficulty.EASY -> "Легко" to AccentGreen
        Difficulty.MEDIUM -> "Средне" to AccentOrange
        Difficulty.HARD -> "Сложно" to AccentRed
    }
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
    }
}
