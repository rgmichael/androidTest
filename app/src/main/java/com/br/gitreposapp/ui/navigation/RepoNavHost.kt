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

    NavHost(navController, startDestination = AppRoutes.REPO_LIST) {
        composable(AppRoutes.REPO_LIST) {
            RepoListScreen(
                onNavigateToFavorites = {
                    navController.navigate(AppRoutes.FAVORITES)
                }
            )
        }
        composable(AppRoutes.FAVORITES) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}