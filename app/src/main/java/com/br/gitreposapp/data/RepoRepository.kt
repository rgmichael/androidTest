package com.br.gitreposapp.data

import com.br.gitreposapp.data.local.RepoEntity
import com.br.gitreposapp.domain.model.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun getRepos(page: Int, pageSize: Int): Flow<List<Repo>>
    suspend fun addFavorite(repo: RepoEntity)
    suspend fun removeFavorite(repo: RepoEntity)
    fun getFavorites(): Flow<List<Repo>>
}