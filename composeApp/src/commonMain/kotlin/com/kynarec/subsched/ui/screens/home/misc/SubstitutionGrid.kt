package com.kynarec.subsched.ui.screens.home.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kynarec.shared.data.models.Substitution
import com.kynarec.subsched.DEFAULT_CARD_SIZE
import com.kynarec.subsched.SubSchedViewModel
import com.kynarec.subsched.util.plus
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubstitutionGrid(substitutions: List<Substitution>, date: String, autoScroll: Boolean = false, viewModel: SubSchedViewModel = koinViewModel()) {
    SelectionContainer {
        val listState = rememberLazyListState()

        val cardSize = viewModel.cardSizeFlow.collectAsStateWithLifecycle(DEFAULT_CARD_SIZE).value

        LaunchedEffect(substitutions) {
            if (substitutions.isNotEmpty() && autoScroll) {
                while (true) {
                    delay(1000L)

                    var continueScrolling = true
                    while (continueScrolling) {
                        val result = listState.scrollBy(1f) // pixels per frame
                        if (result <= 0f) continueScrolling = false
                        delay(16) //  ~60fps
                    }

                    delay(2000L)
                    listState.scrollToItem(0)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(date,
                    modifier = Modifier.padding(8.dp),
                    fontSize = cardSize.defaultFontSize + 8.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    HeaderText("Std.", Modifier.weight(0.6f), fontSize = cardSize.defaultFontSize + 6.sp)
                    HeaderText("Kl.", Modifier.weight(0.8f), fontSize = cardSize.defaultFontSize + 6.sp)
                    HeaderText("Ver.", Modifier.weight(1.2f), fontSize = cardSize.defaultFontSize + 6.sp)
                    HeaderText("Raum", Modifier.weight(0.8f), fontSize = cardSize.defaultFontSize + 6.sp)
                    HeaderText("Info", Modifier.weight(1.5f), fontSize = cardSize.defaultFontSize + 6.sp)
                }

                LazyColumn(state = listState) {
                    items(substitutions) { item ->
                        SubstitutionRow(item)
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.5f
                            )
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SubstitutionRow(item: Substitution, viewModel: SubSchedViewModel = koinViewModel()) {
    val cardSize = viewModel.cardSizeFlow.collectAsStateWithLifecycle(DEFAULT_CARD_SIZE).value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Std.
        Text(
            text = "${item.lesson}.",
            modifier = Modifier.weight(0.6f),
            fontSize = cardSize.defaultFontSize + 8.sp,
            fontWeight = FontWeight.Bold
        )

        // Kl.
        Text(item.className, Modifier.weight(0.8f), fontSize = cardSize.defaultFontSize + 7.sp, )

        // Ver.
        Column(Modifier.weight(1.2f).padding(start = 4.dp)) {
            Text(item.subject, fontSize = cardSize.defaultFontSize + 6.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "${item.absentTeacher.name} ➔ ${item.coveringTeacher.name}",
                fontSize = cardSize.defaultFontSize + 5.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // Raum
        Text(item.room, Modifier.weight(0.8f), fontSize = cardSize.defaultFontSize + 6.sp)

        // Info
        Text(
            text = item.info,
            modifier = Modifier.weight(1.5f),
            fontSize = cardSize.defaultFontSize + 5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier, fontSize: TextUnit) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold
    )
}