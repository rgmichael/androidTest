package com.br.gitreposapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.ui.screens.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenScreenIsLoadingInitially_shouldShowFullScreenLoading() {
        composeTestRule.setContent {
            RepoListScreenContent(
                repos = emptyList(),
                isInitialLoading = true,
                isLoading = false,
                error = null,
            )
        }

        composeTestRule.onNodeWithTag("FullScreenLoading").assertIsDisplayed()
    }

    @Test
    fun whenScreenHasError_shouldShowErrorScreen() {
        composeTestRule.setContent {
            RepoListScreenContent(
                repos = emptyList(),
                isInitialLoading = false,
                isLoading = false,
                error = "Erro de conexão"
            )
        }

        composeTestRule.onNodeWithTag("ErrorScreen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Erro de conexão").assertIsDisplayed()
    }

    @Test
    fun whenScreenHasNoRepos_shouldShowEmptyState() {
        composeTestRule.setContent {
            RepoListScreenContent(
                repos = emptyList(),
                isInitialLoading = false,
                isLoading = false,
                error = null
            )
        }

        composeTestRule.onNodeWithTag("EmptyState").assertIsDisplayed()
    }

    @Test
    fun whenScreenHasRepos_shouldShowRepoItems() {
        val testRepos = listOf(
            Repo(
                id = 1,
                name = "Repo A",
                description = "Descrição A",
                url = "http://github.com/repoA",
                avatarUrl = "",
                isFavorite = false
            ),
            Repo(
                id = 2,
                name = "Repo B",
                description = "Descrição B",
                url = "http://github.com/repoB",
                avatarUrl = "",
                isFavorite = true
            )
        )

        composeTestRule.setContent {
            RepoListScreenContent(
                repos = testRepos,
                isInitialLoading = false,
                isLoading = false,
                error = null
            )
        }

        composeTestRule.onNodeWithText("Repo A").assertExists()
        composeTestRule.onNodeWithText("Repo B").assertExists()
    }

    @Composable
    private fun RepoListScreenContent(
        repos: List<Repo>,
        isInitialLoading: Boolean,
        isLoading: Boolean,
        error: String?
    ) {
        when {
            isInitialLoading -> {
                FullScreenLoading()
            }

            error != null -> {
                ErrorScreen(
                    errorMessage = error,
                    onRetry = {},
                    onDismiss = {},
                )
            }

            repos.isEmpty() -> {
                EmptyState()
            }

            else -> {
                repos.forEach { repo ->
                    com.br.gitreposapp.ui.components.RepoItem(
                        repo = repo,
                        onFavoriteClick = {},
                        onClick = {}
                    )
                }
            }
        }
    }
}
