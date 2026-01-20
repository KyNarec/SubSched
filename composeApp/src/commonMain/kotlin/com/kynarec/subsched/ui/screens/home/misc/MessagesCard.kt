package com.kynarec.subsched.ui.screens.home.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kynarec.shared.data.models.Messages


@Composable
fun MessagesCard(messages: Messages) {
    SelectionContainer {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nachrichten - ${messages.date}",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    HeaderText("Mitteilungen der Schulleitung", Modifier.fillMaxWidth())
                }

                messages.messages.forEachIndexed { index, message ->
                    MessageRow(message)

                    if (index < messages.messages.size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageRow(fullText: String) {
    val lines = fullText.split("\n")
    if (lines.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 12.dp)
    ) {
        var currentIndex = 0

        if (lines[currentIndex].startsWith("Eintrag")) {
            Text(
                text = lines[currentIndex],
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            currentIndex++
        }

        if (currentIndex < lines.size) {
            Text(
                text = lines[currentIndex],
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )
            currentIndex++
        }

        if (currentIndex < lines.size) {
            lines.subList(currentIndex, lines.size).forEach { line ->
                if (line.isNotBlank()) {
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}