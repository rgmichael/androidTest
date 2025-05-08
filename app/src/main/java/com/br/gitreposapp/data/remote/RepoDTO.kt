package com.br.gitreposapp.data.remote

import com.google.gson.annotations.SerializedName

data class RepoDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("html_url") val url: String,
    @SerializedName("owner") val owner: OwnerDTO
) {
    data class OwnerDTO(
        @SerializedName("avatar_url") val avatarUrl: String
    )
}