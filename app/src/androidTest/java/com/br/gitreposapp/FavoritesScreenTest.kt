package com.br.gitreposapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.ui.components.RepoItem
import com.br.gitreposapp.ui.screens.EmptyFavoritesMessage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenScreenLoadsWithEmptyFavorites_shouldShowEmptyMessage() {

        composeTestRule.setContent {
            FavoritesScreenContent(
                favorites = emptyList(),
                onNavigateBack = {},
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithText("Nenhum repositório favoritado").assertExists()
    }

    @Test
    fun whenScreenLoadsWithFavorites_shouldShowListOfRepos() {

        val testRepos = listOf(
            Repo(
                id = 1,
                name = "Repo 1",
                description = "Description 1",
                url = "http://example.com/repo1",
                avatarUrl = "https://avatars.githubusercontent.com/u/128?v=1",
                isFavorite = true
            ),

            Repo(
                id = 2,
                name = "Repo 2",
                description = "Description 2",
                url = "http://example.com/repo2",
                avatarUrl = "https://avatars.githubusercontent.com/u/128?v=2",
                isFavorite = true
            )
        )

        composeTestRule.setContent {
            FavoritesScreenContent(
                favorites = testRepos,
                onNavigateBack = {},
                onToggleFavorite = {}
            )
        }

        composeTestRule.onNodeWithText("Repo 1").assertExists()
        composeTestRule.onNodeWithText("Repo 2").assertExists()
    }

    @Composable
    private fun FavoritesScreenContent(
        favorites: List<Repo>,
        onNavigateBack: () -> Unit,
        onToggleFavorite: (Repo) -> Unit
    ) {
        if (favorites.isEmpty()) {
            EmptyFavoritesMessage()
        } else {
            favorites.forEach { repo ->
                RepoItem(
                    repo = repo,
                    onFavoriteClick = { onToggleFavorite(repo) }
                )
            }
        }
    }
}