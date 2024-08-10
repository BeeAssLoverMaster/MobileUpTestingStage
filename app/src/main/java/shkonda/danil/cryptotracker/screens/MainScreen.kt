package shkonda.danil.cryptotracker.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ChipColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shkonda.danil.cryptotracker.repository.CryptoRepository
import shkonda.danil.cryptotracker.retrofit_builder.RetrofitModule
import shkonda.danil.cryptotracker.states.UiState

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainScreenPrev() {
    val repository = CryptoRepository(RetrofitModule.coinGeckoApi)
    MainScreen(repository)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(repository: CryptoRepository) {
    var state by remember { mutableStateOf<UiState>(UiState.Loading) }
    val coroutineScope = rememberCoroutineScope()

    var selectedCurrency by remember { mutableStateOf("usd") }
    var selectedUsdChip by remember { mutableStateOf(true) }
    var selectedRubChip by remember { mutableStateOf(false) }


    val chipColors = FilterChipDefaults.filterChipColors(
        selectedContainerColor = Color(0xcbffe4c0),
        containerColor = Color(0x1f00001f),
        selectedLabelColor = Color(0xffff9f00),
        labelColor = Color.Black
    )

    //Получаем данные при первом запуске
    LaunchedEffect(selectedCurrency) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) { repository.getCryptoData(selectedCurrency) }
            state = if (result.isSuccess) {
                UiState.Success(result.getOrDefault(emptyList()))
            } else {
                UiState.Error(result.exceptionOrNull()?.message.orEmpty())
            }
        }
    }

    fun toggleChip(currency: String) {
        when (currency) {
            "usd" -> {
                if (!selectedUsdChip) {
                    selectedUsdChip = true
                    selectedRubChip = false
                    selectedCurrency = "usd"
                }
            }

            "rub" -> {
                if (!selectedRubChip) {
                    selectedRubChip = true
                    selectedUsdChip = false
                    selectedCurrency = "rub"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Список криптовалют",
                            fontSize = 20.sp,
                            lineHeight = 23.44.sp,
                            fontWeight = FontWeight(500),
                        )
                    }
                )
                Row(Modifier.padding(start = 16.dp)) {
                    FilterChip(
                        onClick = { toggleChip("usd") },
                        label = {
                            Text(
                                "USD",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight(400),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        },
                        selected = selectedUsdChip,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(89.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = chipColors,
                        border = BorderStroke(0.dp, color = Color.Transparent)
                    )
                    FilterChip(
                        onClick = { toggleChip("rub") },
                        label = {
                            Text(
                                "RUB",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight(400),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        },
                        selected = selectedRubChip,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(89.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = chipColors,
                        border = BorderStroke(0.dp, color = Color.Transparent)
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,   // Толщина полоски
                    color = Color.Gray // Цвет полоски
                )
            }
        }
    ) { paddingValues ->
        // Отображение состояния
        when (state) {
            is UiState.Loading -> Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(44.dp),
                    color = Color(0xFFFF9F00),
                    strokeWidth = 4.dp
                )
            }

            is UiState.Success -> CryptoListScreen(
                (state as UiState.Success).coins,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                selectedCurrency
            )

            is UiState.Error -> Text(
                text = "Error: ${(state as UiState.Error).errorMessage}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

