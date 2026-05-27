package com.lefesafety.liveoff.ui.screen.morse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lefesafety.liveoff.ui.components.MorseOutputMode
import com.lefesafety.liveoff.ui.theme.AccentPurple
import com.lefesafety.liveoff.ui.theme.AccentRed
import com.lefesafety.liveoff.ui.theme.DarkBackground
import com.lefesafety.liveoff.ui.theme.DarkSurface
import com.lefesafety.liveoff.ui.theme.TextPrimary
import com.lefesafety.liveoff.ui.theme.TextSecondary

private data class QuickSignal(val label: String, val morse: String)

private val quickSignals = listOf(
    QuickSignal("SOS", "... --- ..."),
    QuickSignal("Помогите", ".... . .-.. .--"),
    QuickSignal("Да", "-.. .-"),
    QuickSignal("Нет", "-. . -")
)

@Composable
fun MorseTransmitScreen(
    viewModel: MorseTransmitViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Передатчик Морзе",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Text input
        OutlinedTextField(
            value = state.text,
            onValueChange = { viewModel.onTextChange(it) },
            label = { Text("Текст для передачи", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentPurple,
                unfocusedBorderColor = TextSecondary,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = AccentPurple
            ),
            singleLine = false,
            minLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Mode selection
        Text(
            text = "Режим вывода",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .selectableGroup()
        ) {
            ModeOption(
                label = "Фонарик",
                selected = state.selectedMode == MorseOutputMode.FLASHLIGHT,
                onSelect = { viewModel.onModeChange(MorseOutputMode.FLASHLIGHT) }
            )
            ModeOption(
                label = "Звук",
                selected = state.selectedMode == MorseOutputMode.SOUND,
                onSelect = { viewModel.onModeChange(MorseOutputMode.SOUND) }
            )
            ModeOption(
                label = "Вибрация",
                selected = state.selectedMode == MorseOutputMode.VIBRATION,
                onSelect = { viewModel.onModeChange(MorseOutputMode.VIBRATION) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Transmit / Stop buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.transmit() },
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                enabled = !state.isPlaying && state.text.isNotBlank()
            ) {
                Text(
                    text = "Передать",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBackground
                )
            }

            if (state.isPlaying) {
                Button(
                    onClick = { viewModel.stop() },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Text(
                        text = "Стоп",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick signals
        Text(
            text = "Быстрые сигналы",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickSignals.forEach { signal ->
                Button(
                    onClick = { viewModel.transmitQuick(signal.morse) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 4.dp, vertical = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurface)
                ) {
                    Text(
                        text = signal.label,
                        fontSize = 12.sp,
                        color = AccentPurple,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (state.isPlaying) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Передача...",
                fontSize = 14.sp,
                color = AccentPurple,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ModeOption(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onSelect,
                role = Role.RadioButton
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = AccentPurple)
        )
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextPrimary,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
