package com.br.gitreposapp.data.remote

import com.google.gson.annotations.SerializedName
data class SearchRepoResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<RepoDto>
)