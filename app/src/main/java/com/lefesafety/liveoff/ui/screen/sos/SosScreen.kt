package com.lefesafety.liveoff.ui.screen.sos

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Flashlight
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lefesafety.liveoff.ui.theme.AccentRed
import com.lefesafety.liveoff.ui.theme.AccentSosRed
import com.lefesafety.liveoff.ui.theme.DarkSurface
import com.lefesafety.liveoff.ui.theme.TextPrimary
import com.lefesafety.liveoff.ui.theme.TextSecondary

private val SosBackground = Color(0xFF1A0000)
private val ActiveBorder = AccentSosRed
private val AlarmActiveGradientStart = Color(0xFF8B0000)
private val AlarmActiveGradientEnd = Color(0xFFFF2200)

@Composable
fun SosScreen(
    onNavigateToEvacuation: () -> Unit,
    onNavigateToFirstAid: () -> Unit,
    onBack: () -> Unit,
    viewModel: SosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val screenBackground = if (uiState.isQuietMode) {
        Color(0xFF0A0000)
    } else {
        SosBackground
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBackground)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SOS",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = AccentSosRed,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (uiState.isQuietMode) {
            Text(
                text = "Тихий режим",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Signal (alarm)
            item {
                SosActionButton(
                    icon = Icons.Default.VolumeUp,
                    label = "Сигнал",
                    isActive = uiState.isAlarmPlaying,
                    isDisabled = uiState.isQuietMode,
                    onClick = { viewModel.toggleAlarm() }
                )
            }

            // 2. Flashlight
            item {
                SosActionButton(
                    icon = Icons.Default.Flashlight,
                    label = "Фонарик",
                    isActive = uiState.isFlashlightOn,
                    onClick = { viewModel.toggleFlashlight() }
                )
            }

            // 3. Evacuation checklist
            item {
                SosActionButton(
                    icon = Icons.Default.Assignment,
                    label = "Эвакуация",
                    onClick = onNavigateToEvacuation
                )
            }

            // 4. Contacts / phone dialer
            item {
                SosActionButton(
                    icon = Icons.Default.Contacts,
                    label = "Контакты",
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                )
            }

            // 5. First aid
            item {
                SosActionButton(
                    icon = Icons.Default.LocalHospital,
                    label = "Первая помощь",
                    onClick = onNavigateToFirstAid
                )
            }

            // 6. Quiet mode
            item {
                SosActionButton(
                    icon = Icons.Default.Bedtime,
                    label = "Тихий режим",
                    isActive = uiState.isQuietMode,
                    onClick = { viewModel.enableQuietMode() }
                )
            }
        }
    }
}

@Composable
private fun SosActionButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean = false,
    isDisabled: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = if (isActive) {
        DarkSurface
    } else {
        DarkSurface
    }

    val borderModifier = if (isActive) {
        Modifier.border(
            width = 2.dp,
            color = ActiveBorder,
            shape = RoundedCornerShape(12.dp)
        )
    } else {
        Modifier
    }

    val contentAlpha = if (isDisabled) 0.4f else 1f

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .then(borderModifier)
            .clickable(enabled = !isDisabled) { onClick() }
    ) {
        Column(
            modifier = if (isActive) {
                Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(AlarmActiveGradientStart, Color(0xFF2A0000))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            } else {
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) AccentSosRed else TextPrimary.copy(alpha = contentAlpha),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (isActive) AccentSosRed else TextSecondary.copy(alpha = contentAlpha),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
