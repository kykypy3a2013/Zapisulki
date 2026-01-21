package com.example.zapisulki.ui.screens.mainscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.zapisulki.ui.navigation.NavigationDrawer
import com.example.zapisulki.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.rotate
import com.example.zapisulki.R
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope

import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import kotlin.io.path.Path
import kotlin.io.path.moveTo
// Добавьте в начало файла новые импорты:
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image

import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    onFabClick: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Состояние для поиска
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    // Защита от быстрых кликов меню
    var isMenuClickEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    // Флаг, что главный экран полностью загружен
    var isMainScreenReady by remember { mutableStateOf(false) }

    // Получаем данные из ViewModel
    val mediaItems by viewModel.allItems.collectAsState()

    // Получаем текущий маршрут для навигации
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // Сбрасываем поиск при уходе с главного экрана
    LaunchedEffect(currentRoute) {
        if (currentRoute != Screen.Main.route) {
            isSearchVisible = false
            searchText = ""
            isMainScreenReady = false
        } else {
            // Даем время на полную загрузку главного экрана
            delay(100)
            isMainScreenReady = true
        }
    }

    // Защита при возврате на главный экран
    LaunchedEffect(Unit) {
        delay(200) // Задержка при первом открытии
        isMainScreenReady = true
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
                                    Text("Поиск...", color = Color.Gray)
                                },
                                singleLine = true
                            )
                        } else {
                            Text("Записульки")
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (isMenuClickEnabled && isMainScreenReady) {
                                    isMenuClickEnabled = false
                                    coroutineScope.launch {
                                        drawerState.open()
                                        delay(300) // Задержка после открытия меню
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
                                // Скрываем поиск, но текст сохраняется в памяти
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Поиск")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (isMainScreenReady) {
                            onFabClick()
                        }
                    },
                    containerColor = Color(0xFF6200EE),
                    modifier = Modifier.alpha(if (isMainScreenReady) 1f else 0.5f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            }
        ) { paddingValues ->
            if (mediaItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Стрелка привязана к нижнему краю экрана
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd)
                            .padding(end = 100.dp)
                            .padding(bottom = 40.dp) // Отступ от нижнего края (регулируйте по необходимости)
                    ) {
                        // Ваша картинка стрелки
                        Image(
                            painter = painterResource(id = R.drawable.arrow_curve),
                            contentDescription = "Стрелка указывает на кнопку добавления",
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.BottomEnd),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // Основной контент (иконка и текст) по центру экрана
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.NoteAdd,
                            contentDescription = "Нет записей",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF6200EE).copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "Записей пока нет",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Нажмите + чтобы добавить первую запись",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }
    }
}


