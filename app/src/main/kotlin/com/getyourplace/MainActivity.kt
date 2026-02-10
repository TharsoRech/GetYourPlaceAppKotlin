package com.getyourplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Persistence.AppDatabase
import com.getyourplace.Persistence.ItemRepository
import com.getyourplace.Views.Pages.HomePage
import com.getyourplace.Views.Pages.HomeScreen
import com.getyourplace.Views.Pages.SplashScreenView
import kotlinx.coroutines.delay

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val HOME_MAIN = "home_main"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val itemRepository = ItemRepository(database.itemDao())

        // Inicialize seu AuthManager aqui (ajuste conforme seu construtor real)
        val authManager = AuthManager(applicationContext)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Routes.SPLASH
            ) {
                // 1. Tela de Splash
                composable(Routes.SPLASH) {
                    LaunchedEffect(Unit) {
                        delay(2500)
                        // Vai para Welcome e remove o Splash da pilha
                        navController.navigate(Routes.WELCOME) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                    SplashScreenView()
                }

                // 2. Tela de Boas-vindas (A que você postou antes)
                composable(Routes.WELCOME) {
                    HomeScreen(
                        itemRepository = itemRepository,
                        onNavigateToHome = {
                            // Navega para a HomePage principal
                            navController.navigate(Routes.HOME_MAIN) {
                                // Opcional: impede o usuário de voltar para a tela de boas-vindas
                                popUpTo(Routes.WELCOME) { inclusive = true }
                            }
                        }
                    )
                }

                // 3. HomePage (A tela com abas/Bottom Navigation)
                composable(Routes.HOME_MAIN) {
                    HomePage(authManager = authManager)
                }
            }
        }
    }
}