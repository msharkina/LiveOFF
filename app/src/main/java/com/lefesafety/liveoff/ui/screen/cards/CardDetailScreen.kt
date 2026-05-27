package com.lefesafety.liveoff.ui.screen.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CardDetailScreen(
    onBack: () -> Unit,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val card by viewModel.card.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    val isRead by viewModel.isRead.collectAsStateWithLifecycle()

    var detailExpanded by remember { mutableStateOf(false) }

    val accentColor = category?.let { CategoryColors.fromHex(it.accentColor) } ?: AccentOrange

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        TopAppBar(
            title = { },
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

        card?.let { c ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Category label
                category?.let { cat ->
                    Text(
                        text = cat.name.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Title
                Text(
                    text = c.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Meta badges row
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TimeBadge(time = c.estimatedTime)
                    DifficultyBadge(difficulty = c.difficulty)
                    c.tools.forEach { tool ->
                        ToolChip(tool = tool)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Brief steps numbered list with accent-colored left border
                Text(
                    text = "Краткие шаги",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                c.briefSteps.forEachIndexed { index, step ->
                    BriefStepItem(
                        number = index + 1,
                        text = step,
                        accentColor = accentColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Expandable detailed instructions section
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { detailExpanded = !detailExpanded }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Подробная инструкция",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = if (detailExpanded)
                                    Icons.Default.KeyboardArrowUp
                                else
                                    Icons.Default.KeyboardArrowDown,
                                contentDescription = if (detailExpanded) "Свернуть" else "Развернуть",
                                tint = TextSecondary
                            )
                        }

                        AnimatedVisibility(visible = detailExpanded) {
                            Text(
                                text = c.detailedContent,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                )
                            )
                        }
                    }
                }

                // Warnings section
                if (c.warnings.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Предупреждения",
                        style = MaterialTheme.typography.titleMedium,
                        color = AccentRed,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    c.warnings.forEach { warning ->
                        WarningItem(text = warning)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.toggleFavorite() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isFavorite) AccentOrange else TextSecondary
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isFavorite) AccentOrange else TextSecondary.copy(alpha = 0.5f)
                        )
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "В избранное")
                    }

                    Button(
                        onClick = { viewModel.markAsRead() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRead) AccentGreen.copy(alpha = 0.8f) else AccentOrange,
                            contentColor = DarkBackground
                        )
                    ) {
                        Text(
                            text = if (isRead) "Прочитано" else "Отметить прочитанным",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun BriefStepItem(
    number: Int,
    text: String,
    accentColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(48.dp)
                .background(
                    color = accentColor,
                    shape = RoundedCornerShape(2.dp)
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "$number.",
                style = MaterialTheme.typography.bodyMedium,
                color = accentColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp, top = 2.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun WarningItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AccentRed.copy(alpha = 0.08f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = AccentRed,
            modifier = Modifier
                .size(18.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary
        )
    }
}

@Composable
private fun TimeBadge(time: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = DarkSurface
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
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
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
