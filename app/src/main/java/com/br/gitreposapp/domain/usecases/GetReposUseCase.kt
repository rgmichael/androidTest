package com.br.gitreposapp.domain.usecases

import com.br.gitreposapp.data.RepoRepository
import com.br.gitreposapp.domain.model.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReposUseCase @Inject constructor(
    private val repository: RepoRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int): Flow<List<Repo>> {
        return repository.getRepos(page, pageSize)
    }
}