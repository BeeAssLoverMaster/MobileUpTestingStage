package shkonda.danil.cryptotracker.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shkonda.danil.cryptotracker.retrofit_builder.RetrofitModule
import shkonda.danil.cryptotracker.repository.CryptoRepository
import shkonda.danil.cryptotracker.states.UiState

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainScreenPrev() {
    val repository = CryptoRepository(RetrofitModule.coinGeckoApi)
    MainScreen(repository)
}

@Composable
fun MainScreen(repository: CryptoRepository) {
    var state by remember { mutableStateOf<UiState>(UiState.Loading) }
    val coroutineScope = rememberCoroutineScope()

    //Получаем данные при первом запуске
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) { repository.getCryptoData() }
            state = if (result.isSuccess) {
                UiState.Success(result.getOrDefault(emptyList()))
            } else {
                UiState.Error(result.exceptionOrNull()?.message.orEmpty())
            }
        }
    }

    // Отображение состояния
    when (state) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> CryptoListScreen((state as UiState.Success).coins)
        is UiState.Error -> Text(
            text = "Error: ${(state as UiState.Error).errorMessage}",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}