package shkonda.danil.cryptotracker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shkonda.danil.cryptotracker.R
import shkonda.danil.cryptotracker.data_coins.entity.CoinDetailDto
import shkonda.danil.cryptotracker.repository.CoinDetailRepository
import shkonda.danil.cryptotracker.states.CoinDataState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailScreen(
    navController: NavHostController,
    repository: CoinDetailRepository,
    cryptoId: String
) {
    var state by remember { mutableStateOf<CoinDataState>(CoinDataState.Loading) }
    val coroutineScope = rememberCoroutineScope()

    var coinDetail by remember { mutableStateOf<CoinDetailDto?>(null) }

    suspend fun fetchCoinData(cryptoId: String) {
        val result = withContext(Dispatchers.IO) { repository.getCoinData(cryptoId) }
        state = if (result.isSuccess) {
            coinDetail = result.getOrNull()
            CoinDataState.Success(data = result)

        } else {
            CoinDataState.Error(result.exceptionOrNull()?.message.orEmpty())
        }
    }

    LaunchedEffect(cryptoId) {
        fetchCoinData(cryptoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(coinDetail?.name ?: "Криптовалюта") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (state) {
            is CoinDataState.Loading -> Box(
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

            is CoinDataState.Success ->
                Column(
                    Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = coinDetail!!.image.large,
                        contentDescription = "Coin icon",
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Описание",
                        fontWeight = FontWeight(500),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = coinDetail!!.description.en,
                        fontWeight = FontWeight(400),
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Категории",
                        fontWeight = FontWeight(500),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Категории: ${coinDetail!!.categories.joinToString(", ")}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                }

            is CoinDataState.Error ->
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
                                    fetchCoinData(cryptoId)
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