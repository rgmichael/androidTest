package com.br.gitreposapp.domain.model

data class Repo(
    val id: Long,
    val name: String,
    val description: String?,
    val url: String,
    val avatarUrl: String,
    var isFavorite: Boolean = false
)