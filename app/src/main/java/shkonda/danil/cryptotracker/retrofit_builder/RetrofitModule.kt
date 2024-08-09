package shkonda.danil.cryptotracker.retrofit_builder

import retrofit2.Retrofit
import shkonda.danil.cryptotracker.data_coins.api.CoinGeckoAPI
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.coingecko.com/api/v3/"

object RetrofitModule {
    val coinGeckoApi: CoinGeckoAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinGeckoAPI::class.java)
    }
}