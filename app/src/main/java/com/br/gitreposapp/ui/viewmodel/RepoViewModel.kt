package com.br.gitreposapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.domain.usecases.GetReposUseCase
import com.br.gitreposapp.domain.usecases.ToggleFavoriteUseCase
import com.br.gitreposapp.ui.states.RepoListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val getReposUseCase: GetReposUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RepoListUiState())
    val uiState: StateFlow<RepoListUiState> = _uiState

    private val _repos = MutableStateFlow<List<Repo>>(emptyList())
    val repos: StateFlow<List<Repo>> = _repos

    private var currentPage = 1
    private var hasMorePages = true

    init {
        loadInitialRepos()
    }

    fun loadInitialRepos() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            getReposUseCase(currentPage, 15)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Erro ao carregar repositórios",
                        isInitialLoading = false
                    )
                    Log.d("Erro ", e.message ?: "Erro ao carregar repositórios")
                }
                .collect { newRepos ->
                    _repos.value = newRepos
                    currentPage = 2
                    hasMorePages = newRepos.isNotEmpty()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isInitialLoading = false
                    )
                }
        }
    }

    fun loadMoreRepos() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            getReposUseCase(currentPage, 15)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Erro ao carregar mais repositórios",
                        isInitialLoading = false
                    )
                    Log.d("Erro ", e.message ?: "Erro ao carregar mais repositórios")
                }
                .collect { newRepos ->
                    _repos.value = _repos.value + newRepos
                    currentPage++
                    hasMorePages = newRepos.isNotEmpty()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isInitialLoading = false
                    )
                }
        }
    }

    fun toggleFavorite(repo: Repo) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(repo)
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