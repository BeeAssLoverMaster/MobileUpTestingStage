package shkonda.danil.cryptotracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import shkonda.danil.cryptotracker.repository.CoinDetailRepository
import shkonda.danil.cryptotracker.repository.CoinListRepository
import shkonda.danil.cryptotracker.retrofit_builder.RetrofitModule
import shkonda.danil.cryptotracker.screens.CoinDetailScreen
import shkonda.danil.cryptotracker.screens.MainScreen

@Composable
fun NavigationController(navController: NavHostController) {

    val coinListRep = CoinListRepository(RetrofitModule.coinGeckoApi)
    val coinDataRep = CoinDetailRepository(RetrofitModule.coinGeckoApi)

    NavHost(navController, startDestination = "coins_list"){
        composable("coins_list") {
            MainScreen(repository = coinListRep) { coinId ->
                navController.navigate("coin_detail/$coinId")
            }
        }
        composable("coin_detail/{coinId}") { navBackStackEntry ->
            val coinId = navBackStackEntry.arguments?.getString("coinId")
            if (coinId != null) {
                CoinDetailScreen(navController = navController, repository = coinDataRep, cryptoId = coinId)
            }
        }
    }
}