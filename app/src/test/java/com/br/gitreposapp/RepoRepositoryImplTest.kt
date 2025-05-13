package com.br.gitreposapp

import com.br.gitreposapp.data.RepoRepositoryImpl
import com.br.gitreposapp.data.local.RepoDao
import com.br.gitreposapp.data.local.RepoEntity
import com.br.gitreposapp.data.remote.RepoApi
import com.br.gitreposapp.data.remote.RepoDto
import com.br.gitreposapp.domain.model.Repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepoRepositoryImplTest {

    @Mock
    private lateinit var api: RepoApi

    @Mock
    private lateinit var dao: RepoDao

    private lateinit var repository: RepoRepositoryImpl

    @Before
    fun setup() {
        repository = RepoRepositoryImpl(api, dao)
    }

    @Test
    fun `getRepos should return list of Repos with correct favorite status`() = runBlocking {
        // Arrange
        val page = 1
        val pageSize = 10
        val remoteRepos = listOf(
            RepoDto(
                id = 1,
                name = "repo1",
                description = "desc1",
                url = "url1",
                owner = RepoDto.OwnerDto(avatarUrl = "avatar1")
            ),
            RepoDto(
                id = 2,
                name = "repo2",
                description = "desc2",
                url = "url2",
                owner = RepoDto.OwnerDto(avatarUrl = "avatar2")
            )
        )
        val favoriteEntities = listOf(
            RepoEntity(
                id = 1,
                name = "repo1",
                description = "desc1",
                url = "url1",
                avatarUrl = "avatar1"
            )
        )

        whenever(api.getPublicRepos(page * pageSize, pageSize)).thenReturn(remoteRepos)
        whenever(dao.getAllFavorites()).thenReturn(flowOf(favoriteEntities))

        // Act
        val result = repository.getRepos(page, pageSize).first()

        // Assert
        assertEquals(2, result.size)
        assertTrue(result[0].isFavorite) // id 1 is favorite
        assertTrue(!result[1].isFavorite) // id 2 is not favorite
        assertEquals("repo1", result[0].name)
        assertEquals("repo2", result[1].name)
    }

    @Test
    fun `addFavorite should call dao insert with correct entity`() = runBlocking {

        val expectedEntity = RepoEntity(
            id = 1,
            name = "repo1",
            description = "desc1",
            url = "url1",
            avatarUrl = "avatar1"
        )

        repository.addFavorite(expectedEntity)

        verify(dao).insert(expectedEntity)
    }

    @Test
    fun `removeFavorite should call dao delete with correct entity`() = runBlocking {

        val expectedEntity = RepoEntity(
            id = 1,
            name = "repo1",
            description = "desc1",
            url = "url1",
            avatarUrl = "avatar1"
        )

        repository.removeFavorite(expectedEntity)

        verify(dao).delete(expectedEntity)
    }

    @Test
    fun `getFavorites should return list of favorite Repos`() = runBlocking {
        // Arrange
        val favoriteEntities = listOf(
            RepoEntity(
                id = 1,
                name = "repo1",
                description = "desc1",
                url = "url1",
                avatarUrl = "avatar1"
            ),
            RepoEntity(
                id = 2,
                name = "repo2",
                description = "desc2",
                url = "url2",
                avatarUrl = "avatar2"
            )
        )
        val expectedRepos = listOf(
            Repo(
                id = 1,
                name = "repo1",
                description = "desc1",
                url = "url1",
                avatarUrl = "avatar1",
                isFavorite = true
            ),
            Repo(
                id = 2,
                name = "repo2",
                description = "desc2",
                url = "url2",
                avatarUrl = "avatar2",
                isFavorite = true
            )
        )

        whenever(dao.getAllFavorites()).thenReturn(flowOf(favoriteEntities))

        // Act
        val result = repository.getFavorites().first()

        // Assert
        assertEquals(2, result.size)
        assertEquals(expectedRepos, result)
        assertTrue(result.all { it.isFavorite })
    }
}