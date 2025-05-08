package com.br.gitreposapp.data

import com.br.gitreposapp.data.local.RepoDao
import com.br.gitreposapp.data.local.RepoEntity
import com.br.gitreposapp.data.remote.RepoApi
import com.br.gitreposapp.domain.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val api: RepoApi,
    private val dao: RepoDao
) : RepoRepository {

    override fun getRepos(page: Int, pageSize: Int): Flow<List<Repo>> {

        return dao.getAllFavorites()
            .flatMapLatest { favoritesEntities ->
                flow {
                    val apiResponse = api.getPublicRepos(page * pageSize, pageSize)
                    val favorites = favoritesEntities.map { it.id }

                    val repos = apiResponse.map { dto ->
                        Repo(
                            id = dto.id,
                            name = dto.name,
                            description = dto.description,
                            url = dto.url,
                            avatarUrl = dto.owner.avatarUrl,
                            isFavorite = favorites.contains(dto.id)
                        )
                    }
                    emit(repos)
                }
            }
    }

    override suspend fun addFavorite(repo: RepoEntity) {
        dao.insert(repo)
    }

    override suspend fun removeFavorite(repo: RepoEntity) {
        dao.delete(repo)
    }

    override fun getFavorites(): Flow<List<Repo>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomainModel() }
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