package com.example.composenetworkrequest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composenetworkrequest.screens.GetScreen
import com.example.composenetworkrequest.screens.PostScreen
import com.example.composenetworkrequest.ui.theme.ComposeNetworkRequestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeNetworkRequestTheme {
                val navController = rememberNavController()
                val screens =
                    mapOf(Screens.Get.name to R.drawable.get, Screens.Post.name to R.drawable.post)
                val currentScreen by navController.currentBackStackEntryAsState()

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    NavigationBar {
                        screens.forEach {
                            NavigationBarItem(
                                selected = it.key == currentScreen?.destination?.route,
                                label = { Text(it.key, fontFamily = CherryBomb) },
                                onClick = {
                                    navController.navigate(it.key)
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(it.value),
                                        contentDescription = null
                                    )
                                },
                            )
                        }
                    }
                }) { innerPadding ->
                    NavHost(navController = navController, startDestination = Screens.Get.name) {
                        composable(Screens.Get.name) { GetScreen(innerPadding) }
                        composable(Screens.Post.name) { PostScreen(innerPadding) }
                    }
                }
            }
        }
    }
}