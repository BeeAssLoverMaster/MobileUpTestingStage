package shkonda.danil.cryptotracker.states

import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

sealed class CoinListState {
    object Loading: CoinListState()
    data class Success(val coins: List<CoinsDto>): CoinListState()
    data class Error(val errorMessage: String): CoinListState()
}