package shkonda.danil.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import shkonda.danil.cryptotracker.retrofit_builder.RetrofitModule
import shkonda.danil.cryptotracker.repository.CryptoRepository
import shkonda.danil.cryptotracker.screens.MainScreen

class MainActivity : ComponentActivity() {

    private val repository = CryptoRepository(RetrofitModule.coinGeckoApi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(repository = repository)
        }
    }
}
