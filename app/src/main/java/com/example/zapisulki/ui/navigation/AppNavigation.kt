package com.example.zapisulki.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.zapisulki.ui.screens.addscreen.AddFormScreen
import com.example.zapisulki.ui.screens.categoryscreen.CategoryScreen
import com.example.zapisulki.ui.screens.mainscreen.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                navController = navController,
                onFabClick = { navController.navigate(Screen.AddForm.route) }
            )
        }

        composable(Screen.AddForm.route) {
            AddFormScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() }
            )
        }

        // Все категории - теперь передаем navController вместо onBackClick
        composable(
            route = Screen.Movies.route,
            arguments = listOf(navArgument("categoryTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryScreen(
                title = backStackEntry.arguments?.getString("categoryTitle") ?: "Кино",
                navController = navController // Передаем navController
            )
        }

        composable(
            route = Screen.Series.route,
            arguments = listOf(navArgument("categoryTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryScreen(
                title = backStackEntry.arguments?.getString("categoryTitle") ?: "Сериалы",
                navController = navController
            )
        }

        composable(
            route = Screen.Anime.route,
            arguments = listOf(navArgument("categoryTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryScreen(
                title = backStackEntry.arguments?.getString("categoryTitle") ?: "Аниме",
                navController = navController
            )
        }

        composable(
            route = Screen.Games.route,
            arguments = listOf(navArgument("categoryTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryScreen(
                title = backStackEntry.arguments?.getString("categoryTitle") ?: "Игры",
                navController = navController
            )
        }

        composable(
            route = Screen.Books.route,
            arguments = listOf(navArgument("categoryTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryScreen(
                title = backStackEntry.arguments?.getString("categoryTitle") ?: "Книги",
                navController = navController
            )
        }
    }
}