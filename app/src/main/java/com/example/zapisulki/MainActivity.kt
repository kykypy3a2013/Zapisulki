package com.example.zapisulki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.launch
import com.example.zapisulki.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Устанавливаем Splash Screen
        val splashScreen = installSplashScreen()

        // Устанавливаем тему Splash Screen программно
        setTheme(R.style.Theme_Zapisulki_Splash)

        super.onCreate(savedInstanceState)

        // Возвращаем основную тему после Splash Screen
        setTheme(R.style.Theme_Zapisulki)



        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// Определяем маршруты навигации
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object AddForm : Screen("add_form")
    object Movies : Screen("movies")
    object Series : Screen("series")
    object Anime : Screen("anime")
    object Games : Screen("games")
    object Books : Screen("books")
}

// Модель для пунктов меню
data class MenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val route: String
)

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
            AddFormScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.Movies.route) {
            CategoryScreen(
                title = "Кино",
                onBackClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Series.route) {
            CategoryScreen(
                title = "Сериалы",
                onBackClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Anime.route) {
            CategoryScreen(
                title = "Аниме",
                onBackClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Games.route) {
            CategoryScreen(
                title = "Игры",
                onBackClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Books.route) {
            CategoryScreen(
                title = "Книги",
                onBackClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    onFabClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Состояние поиска
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Получаем текущий маршрут для сброса состояния при переходе
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Сброс состояния поиска при уходе с главного экрана
    LaunchedEffect(currentRoute) {
        if (currentRoute != Screen.Main.route) {
            isSearchVisible = false
            searchText = ""
        }
    }

    //  Список пунктов меню!!
    val menuItems = listOf(
        MenuItem(
            id = "home",
            title = "Главный экран",
            icon = Icons.Default.Home,
            route = Screen.Main.route
        ),
        MenuItem(
            id = "movies",
            title = "Кино",
            icon = Icons.Default.Movie,
            route = Screen.Movies.route
        ),
        MenuItem(
            id = "series",
            title = "Сериалы",
            icon = Icons.Default.LiveTv,
            route = Screen.Series.route
        ),
        MenuItem(
            id = "anime",
            title = "Аниме",
            icon = Icons.Default.Animation,
            route = Screen.Anime.route
        ),
        MenuItem(
            id = "games",
            title = "Игры",
            icon = Icons.Default.SportsEsports,
            route = Screen.Games.route
        ),
        MenuItem(
            id = "books",
            title = "Книги",
            icon = Icons.Default.MenuBook,
            route = Screen.Books.route
        )
    )

    // Функция для навигации с закрытием Drawer
    fun navigateWithDrawerClose(route: String) {
        scope.launch { drawerState.close() }
        navController.navigate(route) { launchSingleTop = true }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
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
                    // Иконка приложения сверху, выровнена по левому краю
                    Image(
                        painter = painterResource(id = R.drawable.main_icon),
                        contentDescription = "Иконка приложения",
                        modifier = Modifier
                            .size(180.dp)
                            .align(Alignment.Start),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Название приложения под иконкой, выровнено по правому краю
                    Text(
                        text = "Записульки",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Подзаголовок
                    Text(
                        text = "И всякие сохранялки",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )
                }

                // Разделитель
                Divider(color = Color.LightGray, thickness = 1.dp)

                // Пункт "Главный экран" с выделением
                NavigationDrawerItem(
                    label = { Text("Главный экран", fontWeight = FontWeight.Bold) },
                    selected = navController.currentBackStackEntry?.destination?.route == Screen.Main.route,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Главный экран") },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = Color(0xFF6200EE).copy(alpha = 0.1f),
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = Color(0xFF6200EE),
                        selectedIconColor = Color(0xFF6200EE),
                        unselectedTextColor = Color.Black,
                        unselectedIconColor = Color.Gray
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    onClick = { navigateWithDrawerClose(Screen.Main.route) }
                )

                // Разделитель после главного экрана
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Остальные пункты меню
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(menuItems.size - 1) { index ->
                        val item = menuItems[index + 1]
                        val isSelected = navController.currentBackStackEntry?.destination?.route == item.route

                        NavigationDrawerItem(
                            label = { Text(item.title) },
                            selected = isSelected,
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFF6200EE).copy(alpha = 0.1f),
                                unselectedContainerColor = Color.Transparent,
                                selectedTextColor = Color(0xFF6200EE),
                                selectedIconColor = Color(0xFF6200EE),
                                unselectedTextColor = Color.Black,
                                unselectedIconColor = Color.Gray
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            onClick = { navigateWithDrawerClose(item.route) }
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
                        text = "Версия 1.0.0",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                SearchableTopBar(
                    title = "Главный экран",
                    isSearchVisible = isSearchVisible,
                    searchText = searchText,
                    onSearchTextChange = { searchText = it },
                    onSearchVisibleChange = { isSearchVisible = it },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Меню",
                                tint = Color.White
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Главный экран",
                        tint = Color(0xFF6200EE),
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Добро пожаловать!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Используйте меню слева для навигации",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableTopBar(
    title: String,
    isSearchVisible: Boolean,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchVisibleChange: (Boolean) -> Unit,
    navigationIcon: @Composable () -> Unit
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (isSearchVisible) Alignment.CenterStart else Alignment.Center
            ) {
                if (isSearchVisible) {
                    TextField(
                        value = searchText,
                        onValueChange = onSearchTextChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Введите текст для поиска...",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                            cursorColor = Color.White
                        )
                    )
                } else {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        navigationIcon = navigationIcon,
        actions = {
            IconButton(onClick = { onSearchVisibleChange(!isSearchVisible) }) {
                Icon(
                    imageVector = if (isSearchVisible) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF6200EE),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    title: String,
    onBackClick: () -> Unit
) {
    // Состояние поиска для этого экрана
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Иконка категории
    val icon = when (title) {
        "Кино" -> Icons.Default.Movie
        "Сериалы" -> Icons.Default.LiveTv
        "Аниме" -> Icons.Default.Animation
        "Игры" -> Icons.Default.SportsEsports
        "Книги" -> Icons.Default.MenuBook
        else -> Icons.Default.Category
    }

    Scaffold(
        topBar = {
            SearchableTopBar(
                title = title,
                isSearchVisible = isSearchVisible,
                searchText = searchText,
                onSearchTextChange = { searchText = it },
                onSearchVisibleChange = { isSearchVisible = it },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Здесь будет контент для $title",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFormScreen(onBackClick: () -> Unit) {
    // Состояния для полей формы
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var genreExpanded by remember { mutableStateOf(false) }

    // Список жанров
    val genres = listOf("Кино", "Сериал", "Игра", "Книга", "Аниме")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Добавление",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Действие для сохранения */ }) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Сохранить",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Выпадающий список для выбора жанра
            ExposedDropdownMenuBox(
                expanded = genreExpanded,
                onExpandedChange = { genreExpanded = !genreExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedGenre,
                    onValueChange = {},
                    label = {
                        Text(if (selectedGenre.isEmpty()) "Жанр" else selectedGenre)
                    },
                    placeholder = { Text("Выберите жанр") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = genreExpanded,
                    onDismissRequest = { genreExpanded = false }
                ) {
                    genres.forEach { genre ->
                        DropdownMenuItem(
                            text = { Text(genre) },
                            onClick = {
                                selectedGenre = genre
                                genreExpanded = false
                            }
                        )
                    }
                }
            }

            // Поле для названия
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )

            // Поле для описания
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                minLines = 3,
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка сохранения
            Button(
                onClick = {
                    // Здесь будет логика сохранения
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotEmpty() && selectedGenre.isNotEmpty()
            ) {
                Text("Сохранить")
            }

            // Информация о заполнении полей
            if (title.isEmpty() || selectedGenre.isEmpty()) {
                Text(
                    text = "Заполните обязательные поля: название и жанр",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}