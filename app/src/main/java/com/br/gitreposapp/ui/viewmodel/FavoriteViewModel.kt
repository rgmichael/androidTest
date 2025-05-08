package com.br.gitreposapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.gitreposapp.data.RepoRepository
import com.br.gitreposapp.domain.model.Repo
import com.br.gitreposapp.domain.usecases.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val repository: RepoRepository
) : ViewModel() {

    val favorites: Flow<List<Repo>> = repository.getFavorites()

    fun toggleFavorite(repo: Repo) {
        viewModelScope.launch {
            toggleFavoriteUseCase(repo)
        }
    }
}