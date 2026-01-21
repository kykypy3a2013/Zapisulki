package com.example.zapisulki.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.zapisulki.R

data class MenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun NavigationDrawer(
    navController: NavController,
    onCloseDrawer: () -> Unit
) {
    // Получаем текущий маршрут
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Создаем список всех пунктов меню
    val menuItems = listOf(
        MenuItem("home", "Главный экран", Icons.Default.Home, Screen.Main.route),
        MenuItem("movies", "Кино", Icons.Default.Movie, Screen.getMoviesRoute()),
        MenuItem("series", "Сериалы", Icons.Default.LiveTv, Screen.getSeriesRoute()),
        MenuItem("anime", "Аниме", Icons.Default.Animation, Screen.getAnimeRoute()),
        MenuItem("games", "Игры", Icons.Default.SportsEsports, Screen.getGamesRoute()),
        MenuItem("books", "Книги", Icons.Default.MenuBook, Screen.getBooksRoute())
    )

    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        // Верхняя часть Drawer с фиолетовым фоном
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6200EE))
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.main_icon),
                contentDescription = "Иконка приложения",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Записульки",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "И всякие сохранялки",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Разделитель после шапки
        Divider(color = Color.LightGray, thickness = 1.dp)

        // Пункт "Главный экран" отдельно
        val homeItem = menuItems[0]
        val isHomeSelected = when {
            currentRoute == homeItem.route -> true
            else -> false
        }

        NavigationDrawerItem(
            label = {
                Text("Главный экран", fontWeight = FontWeight.Bold)
            },
            selected = isHomeSelected,
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Главный экран",
                    tint = if (isHomeSelected) Color(0xFF6200EE) else Color.Gray
                )
            },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFF6200EE).copy(alpha = 0.1f),
                unselectedContainerColor = Color.Transparent,
                selectedTextColor = Color(0xFF6200EE),
                selectedIconColor = Color(0xFF6200EE),
                unselectedTextColor = Color.Black,
                unselectedIconColor = Color.Gray
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            onClick = {
                if (!isHomeSelected) {
                    navController.navigate(homeItem.route) {
                        popUpTo(homeItem.route) {
                            inclusive = false
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                onCloseDrawer()
            }
        )

        // РАЗДЕЛИТЕЛЬ после "Главный экран"
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Остальные пункты меню (кроме первого)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(menuItems.size - 1) { index ->
                val item = menuItems[index + 1] // Пропускаем первый элемент

                // Определяем, является ли текущий маршрут активным
                val isSelected = when {
                    // Если текущий маршрут точно совпадает
                    currentRoute == item.route -> true
                    // Если это маршрут категории (проверяем начало строки)
                    item.route.startsWith("movies/") && currentRoute?.startsWith("movies/") == true -> true
                    item.route.startsWith("series/") && currentRoute?.startsWith("series/") == true -> true
                    item.route.startsWith("anime/") && currentRoute?.startsWith("anime/") == true -> true
                    item.route.startsWith("games/") && currentRoute?.startsWith("games/") == true -> true
                    item.route.startsWith("books/") && currentRoute?.startsWith("books/") == true -> true
                    else -> false
                }

                NavigationDrawerItem(
                    label = { Text(item.title) },
                    selected = isSelected,
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) Color(0xFF6200EE) else Color.Gray
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF6200EE).copy(alpha = 0.1f),
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color(0xFF6200EE),
                        selectedIconColor = Color(0xFF6200EE),
                        unselectedTextColor = Color.Black,
                        unselectedIconColor = Color.Gray
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Main.route) {
                                    inclusive = false
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        onCloseDrawer()
                    }
                )
            }
        }

        // Версия приложения внизу Drawer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.3f))
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "Версия 1.0",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}