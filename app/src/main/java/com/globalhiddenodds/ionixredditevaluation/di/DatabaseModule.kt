package com.globalhiddenodds.ionixredditevaluation.di

import android.content.Context
import androidx.room.Room
import com.globalhiddenodds.ionixredditevaluation.datasource.database.AppRoomDatabase
import com.globalhiddenodds.ionixredditevaluation.repository.PostingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun providePostingDao(database: AppRoomDatabase): PostingDao {
        return database.postingDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context):
            AppRoomDatabase {
        return Room.databaseBuilder(
            appContext,
            AppRoomDatabase::class.java,
            "ionix_reddit_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}