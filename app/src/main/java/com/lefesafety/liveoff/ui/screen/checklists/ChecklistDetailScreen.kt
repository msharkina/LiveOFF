package com.lefesafety.liveoff.ui.screen.checklists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lefesafety.liveoff.ui.theme.AccentGreen
import com.lefesafety.liveoff.ui.theme.DarkSurfaceVariant
import com.lefesafety.liveoff.ui.theme.TextPrimary
import com.lefesafety.liveoff.ui.theme.TextSecondary

@Composable
fun ChecklistDetailScreen(
    viewModel: ChecklistDetailViewModel = hiltViewModel()
) {
    val checklist by viewModel.checklist.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()

    val items = checklist?.items ?: emptyList()
    val checkedCount = progress.values.count { it }
    val totalCount = items.size
    val progressFraction = if (totalCount > 0) checkedCount.toFloat() / totalCount else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = checklist?.title ?: "",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = AccentGreen,
            trackColor = DarkSurfaceVariant
        )

        Text(
            text = "$checkedCount / $totalCount",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(items) { index, item ->
                val isChecked = progress[index] == true

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked -> viewModel.toggleItem(index, checked) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AccentGreen,
                            uncheckedColor = TextSecondary,
                            checkmarkColor = MaterialTheme.colorScheme.background
                        )
                    )
                    Text(
                        text = item.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isChecked) TextSecondary else TextPrimary,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.resetProgress() },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkSurfaceVariant,
                contentColor = TextPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Сбросить",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
