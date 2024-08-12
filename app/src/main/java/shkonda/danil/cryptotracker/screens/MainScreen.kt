package shkonda.danil.cryptotracker.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shkonda.danil.cryptotracker.R
import shkonda.danil.cryptotracker.repository.CoinListRepository
import shkonda.danil.cryptotracker.states.CoinListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(repository: CoinListRepository, onCoinClick: (String) -> Unit) {
    // Переменные для управления состояним, корутиной и контекстом экрана
    var state by remember { mutableStateOf<CoinListState>(CoinListState.Loading) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Переменные для управления Chip'сов и обновлением экрана
    var selectedCurrency by remember { mutableStateOf("usd") }
    var selectedUsdChip by remember { mutableStateOf(true) }
    var selectedRubChip by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    // Отдельно настраиваем цвета для валю в chip'ах
    val chipColors = FilterChipDefaults.filterChipColors(
        selectedContainerColor = Color(0xcbffe4c0),
        containerColor = Color(0x1f00001f),
        selectedLabelColor = Color(0xffff9f00),
        labelColor = Color.Black
    )

    suspend fun fetchCoinsData(selectedCurrency: String) {
        // Вся работа должна выполняться здесь, без дополнительного запуска корутины
        val result = withContext(Dispatchers.IO) { repository.getCoinList(selectedCurrency) }
        state = if (result.isSuccess) {
            CoinListState.Success(result.getOrDefault(emptyList()))
        } else {
            val errorMessage = result.exceptionOrNull()?.message.orEmpty()
            // Отображаем ошибку через Snackbar
            snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = "Retry",
                duration = SnackbarDuration.Short
            )
            CoinListState.Error(errorMessage)
        }
    }

    //Получаем данные при первом запуске
    LaunchedEffect(selectedCurrency) {
        fetchCoinsData(selectedCurrency)
    }

    // Функция для контроля выбранной валюты
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

                // Добавляем под TopAppBar полоску в соответствии с дизайном
                HorizontalDivider(
                    thickness = 1.dp,   // Толщина полоски
                    color = Color.Gray // Цвет полоски
                )
            }
        },
        snackbarHost = {
            // Настраиваем всплывающее уведомление об ошибке
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                snackbar = { snackbarData ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)) // Небольшая тень под snackbar
                            .background(Color(0xFFEB5757), RoundedCornerShape(8.dp))
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Произошла ошибка при загрузке",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Отображение состояния
        when (state) {
            // Пока данные не получены отображаем колесо загрузки
            is CoinListState.Loading -> Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(44.dp),
                    color = Color(0xFFFF9F00),
                    strokeWidth = 4.dp
                )
            }

            // При получении данных отображаем их на экране
            is CoinListState.Success -> CoinListScreen(
                (state as CoinListState.Success).coins,
                modifier = Modifier
                    .padding(paddingValues),
                selectedCurrency,
                onCoinClick,
                isRefreshing = isRefreshing,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        fetchCoinsData(selectedCurrency)

                        // Добавляем задержку в секунду для красоты
                        delay(1000)
                        isRefreshing = false
                    }
                }
            )

            // Если получаем ошибку из API, то выводим на экран "заглушку"
            is CoinListState.Error ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center

                ) {
                    Column(
                        Modifier.size(width = 230.dp, height = 237.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bitcoin_image),
                            contentDescription = "Error image",
                            Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.padding(bottom = 13.dp))
                        Text(
                            text = "Произошла какая-то ошибка :(\nПопробуем снова?",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight(400),
                            fontSize = 16.sp

                        )
                        Spacer(modifier = Modifier.padding(bottom = 30.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    fetchCoinsData(selectedCurrency)
                                }
                            },
                            Modifier.size(width = 175.dp, height = 36.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF9F00)
                            )
                        ) {
                            Text(
                                text = "Попробовать".uppercase(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500)
                            )
                        }
                    }

                }
        }
    }
}

