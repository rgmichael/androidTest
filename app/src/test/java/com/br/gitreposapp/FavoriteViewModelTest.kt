package com.br.gitreposapp

import com.br.gitreposapp.data.RepoRepository
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.domain.usecases.ToggleFavoriteUseCase
import com.br.gitreposapp.ui.viewmodel.FavoriteViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class FavoriteViewModelTest {

    @Test
    fun `when no favorites, should emit empty list`() = runTest {

        val mockRepo = mockk<RepoRepository> {
            coEvery { getFavorites() } returns flowOf(emptyList())
        }
        val mockUseCase = mockk<ToggleFavoriteUseCase>()

        val viewModel = FavoriteViewModel(mockUseCase, mockRepo)

        var emittedList: List<Repo>? = null
        viewModel.favorites.collect { list ->
            emittedList = list
        }

        assertTrue(emittedList?.isEmpty() == true)
    }

    @Test
    fun `when has favorites, should emit populated list`() = runTest {

        val fakeRepos = listOf(
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

        val mockRepo = mockk<RepoRepository> {
            coEvery { getFavorites() } returns flowOf(fakeRepos)
        }
        val mockUseCase = mockk<ToggleFavoriteUseCase>()

        val viewModel = FavoriteViewModel(mockUseCase, mockRepo)

        var emittedList: List<Repo>? = null
        viewModel.favorites.collect { list ->
            emittedList = list
        }

        assertEquals(2, emittedList?.size)
        assertEquals("Repo 1", emittedList?.first()?.name)
    }
}