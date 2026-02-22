package com.kynarec.subsched.ui.screens.home.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kynarec.shared.data.models.Messages
import com.kynarec.subsched.DEFAULT_CARD_SIZE
import com.kynarec.subsched.DEFAULT_SCROLL_SPEED
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.util.plus
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MessagesCard(messages: Messages, autoScroll: Boolean = false, viewModel: SubSchedViewModel = koinViewModel()) {
    SelectionContainer {
        val listState = rememberLazyListState()
        val cardSize = viewModel.cardSizeFlow.collectAsStateWithLifecycle(DEFAULT_CARD_SIZE).value
        val scrollSpeed = viewModel.scrollSpeedFlow.collectAsStateWithLifecycle(DEFAULT_SCROLL_SPEED).value


        LaunchedEffect(messages) {
            if (messages.messages.isNotEmpty() && autoScroll) {
                while (true) {
                    delay(1000L)

                    var continueScrolling = true
                    while (continueScrolling) {
                        val result = listState.scrollBy(scrollSpeed.pixelsPerFrame) // pixels per frame
                        if (result <= 0f) continueScrolling = false
                        delay(scrollSpeed.delay) //  ~60fps
                    }

                    delay(2000L)
                    listState.scrollToItem(0)
                }
            }
        }
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
                    HeaderText("Mitteilungen der Schulleitung", Modifier.fillMaxWidth(), fontSize = cardSize.defaultFontSize + 6.sp)
                }
                LazyColumn(state = listState) {
                    itemsIndexed(messages.messages) { index, message ->
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