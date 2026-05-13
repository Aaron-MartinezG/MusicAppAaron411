package com.aaronmg.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
//import com.aaronmg.musicapp.screens.AlbumDetailScreen
import com.aaronmg.musicapp.screens.HomeScreen
import com.aaronmg.musicapp.ui.theme.MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable(route = "home") {
                            HomeScreen(navController = navController)
                        }
//                        composable(
//                            route = "albums/{id}",
//                            arguments = listOf(
//                                navArgument("id") {
//                                    type = NavType.StringType
//                                    nullable = false
//                                }
//                            )
//                        ) { backStack ->
//                            val id = backStack.arguments?.getString("id") ?: ""
//                            AlbumDetailScreen(id = id)
//                        }
                    }
                }
            }
        }
    }
}