package com.br.gitreposapp.data.remote

import com.google.gson.annotations.SerializedName

data class RepoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val url: String,
    @SerializedName("owner") val owner: OwnerDto
) {
    data class OwnerDto(
        @SerializedName("avatar_url") val avatarUrl: String
    )
}