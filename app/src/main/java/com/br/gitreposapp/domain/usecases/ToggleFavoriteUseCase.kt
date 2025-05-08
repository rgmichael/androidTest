package com.br.gitreposapp.domain.usecases

import com.br.gitreposapp.data.RepoRepository
import com.br.gitreposapp.data.local.RepoEntity
import com.br.gitreposapp.domain.model.Repo
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(repo: Repo) {
        if (repo.isFavorite) {
            repository.removeFavorite(repo.toEntity())
        } else {
            repository.addFavorite(repo.toEntity())
        }
    }
}

// Extensão para conversão
fun Repo.toEntity() = RepoEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    url = this.url,
    avatarUrl = this.avatarUrl
)