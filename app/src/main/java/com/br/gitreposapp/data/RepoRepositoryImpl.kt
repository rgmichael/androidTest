package com.br.gitreposapp.data

import com.br.gitreposapp.data.local.RepoDao
import com.br.gitreposapp.data.local.RepoEntity
import com.br.gitreposapp.data.remote.RepoApi
import com.br.gitreposapp.domain.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val api: RepoApi,
    private val dao: RepoDao
) : RepoRepository {

    private val _favoritesUpdates = MutableSharedFlow<Unit>(replay = 1)

    override suspend fun getRepos(page: Int, pageSize: Int): List<Repo> {
        val apiResponse = api.searchRepositories(
            query = "stars:>0",
            page = page,
            perPage = pageSize
        )

        val favorites = dao.getAllFavorites().first()

        return apiResponse.items.map { dto ->
            Repo(
                id = dto.id,
                name = dto.name,
                description = dto.description ?: "No description available",
                url = dto.url,
                avatarUrl = dto.owner.avatarUrl,
                isFavorite = favorites.any { it.id == dto.id }
            )
        }
    }

    override suspend fun addFavorite(repo: RepoEntity) {
        dao.insert(repo)
    }

    override suspend fun removeFavorite(repo: RepoEntity) {
        _favoritesUpdates.emit(Unit)
        dao.delete(repo)
    }

    override fun getFavorites(): Flow<List<Repo>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { entity ->
                entity.toDomainModel()
            }
        }
    }

    override fun observeFavorites(): Flow<List<Repo>> {
        return _favoritesUpdates
            .onStart { emit(Unit) }
            .flatMapLatest {
                dao.getAllFavorites()
                    .map { favorites -> favorites.map { it.toDomainModel() } }
            }
    }
}

fun RepoEntity.toDomainModel() = Repo(
    id = this.id,
    name = this.name,
    description = this.description,
    url = this.url,
    avatarUrl = this.avatarUrl,
    isFavorite = true
)