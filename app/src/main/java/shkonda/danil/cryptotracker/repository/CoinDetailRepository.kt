package shkonda.danil.cryptotracker.repository

import retrofit2.Response
import shkonda.danil.cryptotracker.data_coins.api.CoinGeckoAPI
import shkonda.danil.cryptotracker.data_coins.entity.CoinDetailDto
import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

class CoinDetailRepository(private val api: CoinGeckoAPI) {
    suspend fun getCoinData(coinId: String): Result<CoinDetailDto> {
        return Result.success(api.getCoinDetail(coinId))
//        return Result.failure(Exception("Принудительная остановка"))
    }
}