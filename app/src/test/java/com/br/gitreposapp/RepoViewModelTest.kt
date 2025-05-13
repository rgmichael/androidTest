package com.br.gitreposapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.br.gitreposapp.data.remote.RepoDto
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.domain.usecases.GetReposUseCase
import com.br.gitreposapp.domain.usecases.ToggleFavoriteUseCase
import com.br.gitreposapp.ui.viewmodel.RepoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val getReposUseCase: GetReposUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()
    private lateinit var viewModel: RepoViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Configuração padrão para os mocks
        coEvery { getReposUseCase(any(), any()) } returns flowOf(emptyList())
        coEvery { toggleFavoriteUseCase(any()) } returns Unit

        viewModel = RepoViewModel(getReposUseCase, toggleFavoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggleFavorite should call use case with correct repo`() = runTest {
        // Arrange
        val testRepo =             Repo(
            id = 2,
            name = "repo2",
            description = "desc2",
            url = "url2",
            avatarUrl = "avatar2",
            isFavorite = true
        )

        // Act
        viewModel.toggleFavorite(testRepo)

        // Assert
        coVerify(exactly = 1) { toggleFavoriteUseCase(testRepo) }
    }

    @Test
    fun `toggleFavorite should update error state on failure`() = runTest {
        // Arrange
        val testRepo =             Repo(
            id = 2,
            name = "repo2",
            description = "desc2",
            url = "url2",
            avatarUrl = "avatar2",
            isFavorite = true
        )
        coEvery { toggleFavoriteUseCase(testRepo) } throws Exception("DB error")

        // Act
        viewModel.toggleFavorite(testRepo)

        // Assert
        assertEquals("Erro ao atualizar favorito", viewModel.uiState.value.error)
    }

}