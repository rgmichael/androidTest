package com.br.gitreposapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.domain.usecases.GetReposUseCase
import com.br.gitreposapp.domain.usecases.ToggleFavoriteUseCase
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

    // Estado da UI
    private val _uiState = MutableStateFlow(RepoListUiState())
    val uiState: StateFlow<RepoListUiState> = _uiState

    // Lista paginada
    private val _repos = MutableStateFlow<List<Repo>>(emptyList())
    val repos: StateFlow<List<Repo>> = _repos

    // Controle de paginação
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

            getReposUseCase(currentPage, 15) // Agora getReposUseCase também deve devolver Flow<List<Repo>>
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

            getReposUseCase(currentPage, 15) // Agora getReposUseCase também deve devolver Flow<List<Repo>>
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Erro ao carregar mais repositórios",
                        isInitialLoading = false
                    )
                    Log.d("Erro ", e.message ?: "Erro ao carregar mais repositórios")
                }
                .collect { newRepos ->
                    _repos.value = newRepos
                    currentPage++
                    hasMorePages = newRepos.isNotEmpty()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isInitialLoading = false
                    )
                }
        }
    }
    /*fun loadInitialRepos() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val newRepos = getReposUseCase(page = 1, pageSize = 15)
                _repos.clear()
                _repos.addAll(newRepos)
                currentPage = 2
                hasMorePages = newRepos.isNotEmpty()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isInitialLoading = false
                )
            } catch (e: Exception) {
                Log.d("Erro ", e.message ?: "Erro ao carregar mais repositórios")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar repositórios",
                    isInitialLoading = false
                )
            }
        }
    }*/


/*
    fun loadMoreRepos() {
        if (_uiState.value.isLoading || !hasMorePages) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val newRepos = getReposUseCase(currentPage, 15)
                _repos.addAll(newRepos)
                currentPage++
                hasMorePages = newRepos.isNotEmpty()

                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.d("Erro ", e.message ?: "Erro ao carregar mais repositórios")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erro ao carregar mais repositórios"
                )
            }
        }
    }
*/
    fun toggleFavorite(repo: Repo) {
        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(repo)
                // Atualização automática: não precisa manualmente mudar o item na lista!
                // A stream dos repos já reflete o novo favorito.
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

// Modelo de estado da UI
data class RepoListUiState(
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true,
    val error: String? = null
)