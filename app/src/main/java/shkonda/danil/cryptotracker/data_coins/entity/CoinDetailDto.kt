package shkonda.danil.cryptotracker.data_coins.entity

data class CoinDetailDto(
    val id: String,
    val symbol: String,
    val name: String,
    val description: Description,
    val image: Image,
    val categories: List<String>
)

data class Description(
    val en: String
)

data class Image(
    val large: String
)