package com.br.gitreposapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.br.gitreposapp.ui.screens.FavoritesScreen
import com.br.gitreposapp.ui.screens.RepoListScreen

@Composable
fun RepoNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "repo_list") {
        composable("repo_list") {
            RepoListScreen(navController = navController)
        }
        composable("favorites") {
            FavoritesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}