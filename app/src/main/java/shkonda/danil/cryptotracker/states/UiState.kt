package shkonda.danil.cryptotracker.states

import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

sealed class UiState {
    object Loading: UiState()
    data class Success(val coins: List<CoinsDto>): UiState()
    data class Error(val errorMessage: String): UiState()
}