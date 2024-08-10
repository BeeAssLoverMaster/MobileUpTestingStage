package shkonda.danil.cryptotracker.data_coins.entity

import com.google.gson.annotations.SerializedName

data class CoinsDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage: Double
)
