package com.globalhiddenodds.ionixredditevaluation.datasource.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globalhiddenodds.ionixredditevaluation.ui.data.PostingView

@Entity(tableName = "posting")
data class Posting(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "idPost")
    val idPost: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "score")
    val score: Double,
    @ColumnInfo(name = "comments")
    val comments: Double
)

fun Posting.toPostingView():
        PostingView = PostingView(
    id, idPost, title, url, score, comments
)