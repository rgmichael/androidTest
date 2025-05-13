package com.br.gitreposapp

import com.br.gitreposapp.data.RepoRepository
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.domain.usecases.GetReposUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetReposUseCaseTest {

    @Mock
    private lateinit var repository: RepoRepository

    private lateinit var useCase: GetReposUseCase

    @Test
    fun `invoke should call repository getRepos with correct parameters`() = runBlocking {
        // Arrange
        useCase = GetReposUseCase(repository)
        val page = 1
        val pageSize = 10
        val expectedRepos = listOf(
            Repo(
                id = 1,
                name = "repo1",
                description = "desc1",
                url = "url1",
                avatarUrl = "avatar1",
                isFavorite = false
            )
        )

        whenever(repository.getRepos(page, pageSize)).thenReturn(flowOf(expectedRepos))

        // Act
        val result = useCase(page, pageSize).first()

        // Assert
        verify(repository).getRepos(page, pageSize)
        assertEquals(expectedRepos, result)
    }

    @Test
    fun `invoke should return flow from repository`() = runBlocking {
        // Arrange
        useCase = GetReposUseCase(repository)
        val page = 2
        val pageSize = 20
        val expectedRepos = listOf(
            Repo(
                id = 2,
                name = "repo2",
                description = "desc2",
                url = "url2",
                avatarUrl = "avatar2",
                isFavorite = true
            )
        )

        whenever(repository.getRepos(page, pageSize)).thenReturn(flowOf(expectedRepos))

        // Act
        val result = useCase(page, pageSize).first()

        // Assert
        assertEquals(expectedRepos.size, result.size)
        assertEquals(expectedRepos[0].id, result[0].id)
        assertEquals(expectedRepos[0].isFavorite, result[0].isFavorite)
    }
}