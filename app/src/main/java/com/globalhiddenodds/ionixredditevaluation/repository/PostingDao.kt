package com.globalhiddenodds.ionixredditevaluation.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globalhiddenodds.ionixredditevaluation.datasource.database.data.Posting
import kotlinx.coroutines.flow.Flow

@Dao
interface PostingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(posting: Posting)

    @Query("SELECT * FROM posting")
    fun getPosting(): Flow<List<Posting>>

    @Query("DELETE FROM posting")
    suspend fun deletePostings()
}