package com.br.gitreposapp

import com.br.gitreposapp.data.remote.RepoApi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepoApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: RepoApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // <-- usa o servidor mockado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RepoApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getPublicRepos should parse response correctly`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                [
                  {
                    "id": 123,
                    "name": "TestRepo",
                    "description": "A test repository",
                    "html_url": "http://github.com/test",
                    "owner": {
                      "avatar_url": "http://avatar.com/avatar.png"
                    }
                  }
                ]
            """.trimIndent())

        mockWebServer.enqueue(mockResponse)

        val result = api.getPublicRepos(since = 0)

        assertEquals(1, result.size)
        val repo = result[0]
        assertEquals(123, repo.id)
        assertEquals("TestRepo", repo.name)
        assertEquals("A test repository", repo.description)
        assertEquals("http://github.com/test", repo.url)
        assertEquals("http://avatar.com/avatar.png", repo.owner.avatarUrl)
    }

    @Test
    fun getPublicRepos_whenServerError_shouldThrow() = runTest {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        // Act & Assert
        try {
            api.getPublicRepos(since = 0)
            fail("Should have thrown exception")
        } catch (e: Exception) {
            // Esperado
        }
    }
}