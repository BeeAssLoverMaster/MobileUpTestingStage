package shkonda.danil.cryptotracker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import shkonda.danil.cryptotracker.components.PullToRefreshLazy
import shkonda.danil.cryptotracker.data_coins.entity.CoinsDto
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CoinListScreen(
    coins: List<CoinsDto>,
    modifier: Modifier,
    selectedCurrency: String,
    onCoinClick: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    PullToRefreshLazy(
        items = coins,
        content = { crypto ->
            CryptoItem(crypto, selectedCurrency, onClick = { onCoinClick(crypto.id) })
        },
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    )
    println(isRefreshing)
}

@Composable
fun CryptoItem(
    crypto: CoinsDto,
    selectedCurrency: String,
    onClick: () -> Unit
) {
    // Для чисел с большим значением добавляем разбиение на сотни и десятки
    val formattedCurrentPrice = NumberFormat.getNumberInstance(Locale.US).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }.format(crypto.current_price)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = crypto.image,
            contentDescription = "Coin icon",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(start = 8.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    crypto.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF525252)
                )
                Text(
                    text = if (selectedCurrency == "usd") "$ $formattedCurrentPrice" else "₽ $formattedCurrentPrice",
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF525252)
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    crypto.symbol.uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9B9B9B)
                )
                Text(
                    text = String.format("%.2f%%", crypto.priceChangePercentage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = if (crypto.priceChangePercentage > 0) Color(0xFF2A9D8F) else Color(
                        0xFFEB5757
                    )
                )
            }
        }
    }
}
