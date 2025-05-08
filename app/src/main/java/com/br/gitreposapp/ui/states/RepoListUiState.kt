package com.br.gitreposapp.ui.states

data class RepoListUiState(
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true,
    val error: String? = null
)