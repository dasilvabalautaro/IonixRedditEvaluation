package com.globalhiddenodds.ionixredditevaluation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.globalhiddenodds.ionixredditevaluation.datasource.database.AppRoomDatabase
import com.globalhiddenodds.ionixredditevaluation.datasource.database.data.Posting
import com.globalhiddenodds.ionixredditevaluation.repository.PostingDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DatabaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var postingDao: PostingDao
    private lateinit var db: AppRoomDatabase

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        postingDao = db.postingDao()
    }

    @After
    fun teardown(){
        db.close()
    }

    @Test
    fun insertPosting() = runTest {
        val post = Posting(1, "vcx", "Cursed",
            "https://i.redd.it/uz7uktmik7691.jpg", 124.0, 1.0)
        postingDao.insert(post)
        val postInserted = postingDao.getPosting()
            .asLiveData().getOrAwaitValue()
        assertThat(postInserted.size).isEqualTo(1)
    }
}