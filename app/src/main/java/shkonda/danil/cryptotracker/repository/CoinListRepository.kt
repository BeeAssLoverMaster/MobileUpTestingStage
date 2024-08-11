package shkonda.danil.cryptotracker.repository

import retrofit2.HttpException
import shkonda.danil.cryptotracker.data_coins.api.CoinGeckoAPI
import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

class CoinListRepository(private val api: CoinGeckoAPI) {
    suspend fun getCoinList(currency: String): Result<List<CoinsDto>> {
        return try {
            Result.success(api.getCoin_N_List(currency))
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