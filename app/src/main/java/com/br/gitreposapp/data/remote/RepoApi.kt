package com.br.gitreposapp.data.remote

import com.br.marveltestnew.data.remote.RepoDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoApi {
    @GET("repositories")
    suspend fun getPublicRepos(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int = 15
    ): List<RepoDTO>
}