package com.br.gitreposapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RepoApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchRepoResponse
}