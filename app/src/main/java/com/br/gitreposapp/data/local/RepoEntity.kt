package com.br.gitreposapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val url: String,
    val avatarUrl: String
)