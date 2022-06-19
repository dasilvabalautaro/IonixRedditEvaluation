package com.globalhiddenodds.ionixredditevaluation.datasource.network.data

import com.globalhiddenodds.ionixredditevaluation.datasource.database.data.Posting

data class PostingCloud(
    val idPost: String,
    val title: String,
    val url: String,
    val score: Double,
    val comments: Double
) {
    fun toPosting(): Posting {
        return Posting(0, idPost, title, url, score, comments)
    }

}