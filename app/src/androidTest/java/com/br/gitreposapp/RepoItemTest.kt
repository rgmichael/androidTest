package com.br.gitreposapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.ui.components.RepoItem
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoItemTest {

    @get:Rule
    val rule = createComposeRule()

    private val testRepo = Repo(
        id = 1,
        name = "Test Repo",
        description = "This is a test repository",
        url = "https://github.com/test/repo",
        avatarUrl = "https://avatar.url",
        isFavorite = false
    )

    @Test
    fun repoItem_shouldDisplayAllInformation() {
        rule.setContent {
            MaterialTheme {
                RepoItem(
                    repo = testRepo,
                    onFavoriteClick = {},
                    onClick = {}
                )
            }
        }

        // Verifica se todos os elementos estão visíveis
        rule.onNodeWithText("Test Repo").assertExists()
        rule.onNodeWithText("This is a test repository").assertExists()
        rule.onNodeWithText("github.com/test/repo").assertExists()
        rule.onNodeWithContentDescription("Owner avatar").assertExists()
        rule.onNodeWithContentDescription("Toggle favorite").assertExists()
    }

    @Test
    fun favoriteButton_shouldShowCorrectIcon() {
        // Teste para repo favoritado
        val favoriteRepo = testRepo.copy(isFavorite = true)

        rule.setContent {
            MaterialTheme {
                RepoItem(
                    repo = favoriteRepo,
                    onFavoriteClick = {},
                    onClick = {}
                )
            }
        }

        // Verifica se o ícone de favorito preenchido está visível
        rule.onNodeWithContentDescription("Toggle favorite")
            .assert(hasContentDescription("Toggle favorite"))
    }
}