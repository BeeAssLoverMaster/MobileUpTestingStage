package shkonda.danil.cryptotracker.states

import shkonda.danil.cryptotracker.data_coins.entity.CoinDetailDto

sealed class CoinDataState {
    object Loading: CoinDataState()
    data class Success(val data: Result<CoinDetailDto>): CoinDataState()
    data class Error(val errorMessage: String): CoinDataState()
}