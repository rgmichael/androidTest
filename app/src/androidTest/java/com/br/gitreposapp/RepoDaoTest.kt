package com.br.gitreposapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.br.gitreposapp.data.local.AppDatabase
import com.br.gitreposapp.data.local.RepoDao
import com.br.gitreposapp.data.local.RepoEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var repoDao: RepoDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repoDao = database.repoDao()  // Certifique-se que está obtendo o DAO corretamente
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetRepo_shouldWorkCorrectly() = runTest {
        // Arrange
        val repo = RepoEntity(
            id = 1L,
            name = "Test Repo",
            description = "Test Description",
            url = "http://test.com",
            avatarUrl = "http://avatar.com"
        )

        // Act
        repoDao.insert(repo)
        val repos = repoDao.getAllFavorites().first()

        // Assert
        assertEquals(1, repos.size)
        assertEquals(repo.id, repos[0].id)
        assertEquals(repo.name, repos[0].name)
    }

    @Test
    fun insert_withSameId_shouldReplaceExisting() = runTest {
        // Arrange
        val repo1 = RepoEntity(
            id = 1L,
            name = "Repo 1",
            description = "Desc 1",
            url = "http://test.com/1",
            avatarUrl = "http://avatar.com/1"
        )

        val repo2 = RepoEntity(
            id = 1L, // Mesmo ID
            name = "Repo 2",
            description = "Desc 2",
            url = "http://test.com/2",
            avatarUrl = "http://avatar.com/2"
        )

        // Act
        repoDao.insert(repo1)
        repoDao.insert(repo2)
        val repos = repoDao.getAllFavorites().first()

        // Assert
        assertEquals(1, repos.size)
        assertEquals("Repo 2", repos[0].name)
        assertEquals("Desc 2", repos[0].description)
    }

    @Test
    fun deleteRepo_shouldRemoveFromDatabase() = runTest {
        // Arrange
        val repo = RepoEntity(
            id = 1L,
            name = "Test Repo",
            description = "Test Description",
            url = "http://test.com",
            avatarUrl = "http://avatar.com"
        )

        // Act
        repoDao.insert(repo)
        repoDao.delete(repo)
        val repos = repoDao.getAllFavorites().first()

        // Assert
        assertEquals(0, repos.size)
    }
}