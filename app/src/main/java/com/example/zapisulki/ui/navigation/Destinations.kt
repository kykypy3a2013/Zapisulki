package com.example.zapisulki.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object AddForm : Screen("add_form")
    object Movies : Screen("movies/{categoryTitle}")
    object Series : Screen("series/{categoryTitle}")
    object Anime : Screen("anime/{categoryTitle}")
    object Games : Screen("games/{categoryTitle}")
    object Books : Screen("books/{categoryTitle}")

    companion object {
        fun getMoviesRoute() = "movies/Кино"
        fun getSeriesRoute() = "series/Сериалы"
        fun getAnimeRoute() = "anime/Аниме"
        fun getGamesRoute() = "games/Игры"
        fun getBooksRoute() = "books/Книги"
    }
}