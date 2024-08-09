package shkonda.danil.cryptotracker.repository

import shkonda.danil.cryptotracker.data_coins.api.CoinGeckoAPI
import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

class CryptoRepository(private val api: CoinGeckoAPI) {
    suspend fun getCryptoData(): Result<List<CoinsDto>> {
        return Result.success(api.getAllCoins("usd"))
//        return Result.failure(Exception("Принудительная остановка"))
    }
}