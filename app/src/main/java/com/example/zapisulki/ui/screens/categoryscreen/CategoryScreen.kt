package com.example.zapisulki.ui.screens.categoryscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.zapisulki.ui.navigation.NavigationDrawer
import com.example.zapisulki.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    title: String,
    navController: NavHostController, // Изменяем: вместо onBackClick принимаем navController
    viewModel: CategoryViewModel = viewModel(factory = CategoryViewModel.provideFactory(title))
) {
    // Добавляем DrawerState как в MainScreen
    val drawerState = rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Состояние для поиска
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Защита от быстрых кликов меню
    var isMenuClickEnabled by remember { mutableStateOf(true) }

    // Получаем элементы категории
    val categoryItems by viewModel.categoryItems.collectAsState()

    // Получаем текущий маршрут
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // Сбрасываем поиск при изменении категории
    LaunchedEffect(title) {
        isSearchVisible = false
        searchText = ""
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                navController = navController,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6200EE),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    title = {
                        if (isSearchVisible) {
                            TextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    cursorColor = Color.Black
                                ),
                                placeholder = {
                                    Text("Поиск в $title...", color = Color.Gray)
                                },
                                singleLine = true
                            )
                        } else {
                            Text(title)
                        }
                    },
                    navigationIcon = {
                        // Заменяем кнопку "Назад" на кнопку "Меню"
                        IconButton(
                            onClick = {
                                if (isMenuClickEnabled) {
                                    isMenuClickEnabled = false
                                    scope.launch {
                                        drawerState.open()
                                        kotlinx.coroutines.delay(300)
                                        isMenuClickEnabled = true
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isSearchVisible = !isSearchVisible
                            if (!isSearchVisible) {
                                // Скрываем поиск
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Поиск")
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (categoryItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Book,
                            contentDescription = "Нет записей",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF6200EE).copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "В категории \"$title\" пока нет записей",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center // ← ДОБАВИТЬ ЭТУ СТРОКУ
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Вернитесь на главный экран и добавьте запись",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

