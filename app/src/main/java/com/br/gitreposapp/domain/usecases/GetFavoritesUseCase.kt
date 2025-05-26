package com.br.gitreposapp.domain.usecases

import com.br.gitreposapp.data.RepoRepository
import com.br.gitreposapp.domain.model.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    operator fun invoke(): Flow<List<Repo>> {
        return repository.getFavorites()
    }
}