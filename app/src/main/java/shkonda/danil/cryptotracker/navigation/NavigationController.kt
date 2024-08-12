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

    // Экземпляры репозиториев, хранящие данные, полученные из API
    // В дальнейшем передаются в соответствующие экраны для обработки данных
    val coinListRep = CoinListRepository(RetrofitModule.coinGeckoApi)   // Список монет
    val coinDataRep = CoinDetailRepository(RetrofitModule.coinGeckoApi) // Подробные данные о монете

    // Определяем структуру навигации приложения с помощью NavHost
    NavHost(navController, startDestination = "coins_list") {

        // Определяем маршрут до главного экрана со списком всех монет
        composable("coins_list") {
            // При нажатии на монеты вызывается лямбда-функция и происходит навигация на экран подробной информации
            MainScreen(repository = coinListRep) { coinId ->
                navController.navigate("coin_detail/$coinId")
            }
        }
        // Определяем маршрут до экрана с подробным описанием монеты на основе переданной coinId
        composable("coin_detail/{coinId}") { navBackStackEntry ->
            val coinId = navBackStackEntry.arguments?.getString("coinId")
            if (coinId != null) {
                CoinDetailScreen(
                    navController = navController,
                    repository = coinDataRep,
                    cryptoId = coinId
                )
            }
        }
    }
}