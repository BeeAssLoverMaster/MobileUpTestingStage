package shkonda.danil.cryptotracker.repository

import retrofit2.HttpException
import retrofit2.Response
import shkonda.danil.cryptotracker.data_coins.api.CoinGeckoAPI
import shkonda.danil.cryptotracker.data_coins.entity.CoinDetailDto
import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

class CoinDetailRepository(private val api: CoinGeckoAPI) {
    suspend fun getCoinData(coinId: String): Result<CoinDetailDto> {
        return try {
            Result.success(api.getCoinDetail(coinId))
        } catch (e: HttpException) {
            if (e.code() == 429) {
                Result.failure(Exception("Превышен лимит запросов"))
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}