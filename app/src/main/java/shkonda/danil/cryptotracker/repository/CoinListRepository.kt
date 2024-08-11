package shkonda.danil.cryptotracker.repository

import shkonda.danil.cryptotracker.data_coins.api.CoinGeckoAPI
import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

class CoinListRepository(private val api: CoinGeckoAPI) {
    suspend fun getCoinList(currency: String): Result<List<CoinsDto>> {
        return Result.success(api.getCoin_N_List(currency))
//        return Result.failure(Exception("Принудительная остановка"))
    }
}