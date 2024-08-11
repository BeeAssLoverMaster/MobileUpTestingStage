package shkonda.danil.cryptotracker.data_coins.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import shkonda.danil.cryptotracker.data_coins.entity.CoinDetailDto
import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto

interface CoinGeckoAPI {
    @GET("coins/markets")
    suspend fun getCoin_N_List(
        @Query("vs_currency") currency: String,
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<CoinsDto>

    @GET("coins/{id}")
    suspend fun getCoinDetail(
        @Path("id") id: String
    ): CoinDetailDto
}