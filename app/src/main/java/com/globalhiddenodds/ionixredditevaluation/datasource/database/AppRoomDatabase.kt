package com.globalhiddenodds.ionixredditevaluation.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globalhiddenodds.ionixredditevaluation.datasource.database.data.Posting
import com.globalhiddenodds.ionixredditevaluation.repository.PostingDao

@Database(entities = [Posting::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun postingDao(): PostingDao
}