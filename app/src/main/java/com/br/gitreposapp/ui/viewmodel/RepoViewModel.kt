package com.br.gitreposapp.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.domain.usecases.GetReposUseCase
import com.br.gitreposapp.domain.usecases.ObserveFavoritesUseCase
import com.br.gitreposapp.domain.usecases.ToggleFavoriteUseCase
import com.br.gitreposapp.ui.states.RepoListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val getReposUseCase: GetReposUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepoListUiState())
    val uiState: StateFlow<RepoListUiState> = _uiState

    private val _repos = mutableStateListOf<Repo>()
    val repos: List<Repo> get() = _repos

    private var currentPage = 1
    private var hasMorePages = true
    private val loadedIds = mutableSetOf<Long>()

    init {
        loadInitialRepos()
        setupFavoriteObservation()
    }

    private fun setupFavoriteObservation() {
        viewModelScope.launch {
            observeFavoritesUseCase()
                .collect { favorites ->
                    updateFavoritesStatus(favorites)
                }
        }
    }

    private fun updateFavoritesStatus(favorites: List<Repo>) {
        _repos.forEachIndexed { index, repo ->
            val isFavorite = favorites.any { it.id == repo.id }
            if (repo.isFavorite != isFavorite) {
                _repos[index] = repo.copy(isFavorite = isFavorite)
            }
        }
    }

    fun loadInitialRepos() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isInitialLoading = true,
                error = null
            )

            loadedIds.clear()

            try {
                val newRepos = getReposUseCase(page = 1, pageSize = 15)
                loadedIds.addAll(newRepos.map { it.id })
                _repos.clear()
                _repos.addAll(newRepos)
                currentPage = 2
                hasMorePages = newRepos.size == 15

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isInitialLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isInitialLoading = false,
                    error = "Erro ao carregar repositórios"
                )
            }
        }
    }

    fun loadMoreRepos() {
        if (_uiState.value.isLoading || !hasMorePages) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val newRepos = getReposUseCase(currentPage, 15)
                val uniqueRepos = newRepos.filterNot { loadedIds.contains(it.id) }

                if (uniqueRepos.isNotEmpty()) {
                    loadedIds.addAll(uniqueRepos.map { it.id })
                    _repos.addAll(uniqueRepos)
                    currentPage++
                }

                hasMorePages = newRepos.size == 15

                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar mais repositórios"
                )
            }
        }
    }

    fun toggleFavorite(repo: Repo) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(repo)
                val index = _repos.indexOfFirst { it.id == repo.id }
                if (index >= 0) {
                    _repos[index] = _repos[index].copy(isFavorite = !repo.isFavorite)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao atualizar favorito"
                )
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}