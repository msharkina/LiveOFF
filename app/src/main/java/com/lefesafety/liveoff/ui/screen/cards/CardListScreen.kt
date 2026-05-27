package com.lefesafety.liveoff.ui.screen.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lefesafety.liveoff.domain.model.Card
import com.lefesafety.liveoff.domain.model.Difficulty
import com.lefesafety.liveoff.ui.theme.AccentGreen
import com.lefesafety.liveoff.ui.theme.AccentOrange
import com.lefesafety.liveoff.ui.theme.AccentRed
import com.lefesafety.liveoff.ui.theme.CategoryColors
import com.lefesafety.liveoff.ui.theme.DarkBackground
import com.lefesafety.liveoff.ui.theme.DarkSurface
import com.lefesafety.liveoff.ui.theme.DarkSurfaceVariant
import com.lefesafety.liveoff.ui.theme.TextPrimary
import com.lefesafety.liveoff.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    onNavigateToCardDetail: (cardId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: CardListViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()

    val accentColor = category?.let { CategoryColors.fromHex(it.accentColor) } ?: AccentOrange

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = category?.name ?: "",
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = TextPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkBackground
            )
        )

        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(cards) { card ->
                CardListItem(
                    card = card,
                    accentColor = accentColor,
                    onClick = { onNavigateToCardDetail(card.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CardListItem(
    card: Card,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = card.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyBadge(difficulty = card.difficulty)
                Text(
                    text = card.estimatedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            if (card.tools.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    card.tools.forEach { tool ->
                        ToolChip(tool = tool)
                    }
                }
            }
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
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun ToolChip(tool: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = DarkSurfaceVariant
    ) {
        Text(
            text = tool,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
