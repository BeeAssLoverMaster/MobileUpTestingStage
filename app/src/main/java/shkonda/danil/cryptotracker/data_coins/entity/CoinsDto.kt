package shkonda.danil.cryptotracker.data_coins.entity

data class CoinsDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double
)
