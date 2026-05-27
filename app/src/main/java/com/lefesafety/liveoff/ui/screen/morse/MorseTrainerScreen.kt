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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lefesafety.liveoff.ui.theme.AccentGreen
import com.lefesafety.liveoff.ui.theme.AccentPurple
import com.lefesafety.liveoff.ui.theme.AccentRed
import com.lefesafety.liveoff.ui.theme.DarkBackground
import com.lefesafety.liveoff.ui.theme.DarkSurface
import com.lefesafety.liveoff.ui.theme.TextPrimary
import com.lefesafety.liveoff.ui.theme.TextSecondary

@Composable
fun MorseTrainerScreen(
    viewModel: MorseTrainerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val resultColor = when (state.checkResult) {
        CheckResult.CORRECT -> AccentGreen
        CheckResult.WRONG -> AccentRed
        CheckResult.NONE -> DarkBackground
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Тренажёр Морзе",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Правильно: ${state.correctCount} / ${state.totalCount}",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Current letter display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (state.checkResult != CheckResult.NONE) resultColor.copy(alpha = 0.15f)
                    else DarkSurface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.currentLetter.toString(),
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = AccentPurple
            )
            if (state.checkResult != CheckResult.NONE) {
                Text(
                    text = state.currentMorse,
                    fontSize = 20.sp,
                    color = if (state.checkResult == CheckResult.CORRECT) AccentGreen else AccentRed,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = if (state.checkResult == CheckResult.CORRECT) "Верно!" else "Неверно",
                    fontSize = 16.sp,
                    color = if (state.checkResult == CheckResult.CORRECT) AccentGreen else AccentRed,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User input display
        Text(
            text = if (state.userInput.isEmpty()) "—" else state.userInput,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Dot / Dash buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.appendDot() },
                modifier = Modifier.weight(1f).height(64.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                enabled = state.checkResult == CheckResult.NONE
            ) {
                Text(
                    text = "Точка  •",
                    fontSize = 20.sp,
                    color = TextPrimary
                )
            }
            Button(
                onClick = { viewModel.appendDash() },
                modifier = Modifier.weight(1f).height(64.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                enabled = state.checkResult == CheckResult.NONE
            ) {
                Text(
                    text = "Тире  —",
                    fontSize = 20.sp,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Check button
        Button(
            onClick = { viewModel.checkAnswer() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
            enabled = state.checkResult == CheckResult.NONE && state.userInput.isNotEmpty()
        ) {
            Text(
                text = "Проверить",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBackground
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Next button
        Button(
            onClick = { viewModel.nextLetter() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkSurface)
        ) {
            Text(
                text = "Следующая",
                fontSize = 16.sp,
                color = TextPrimary
            )
        }
    }
}
