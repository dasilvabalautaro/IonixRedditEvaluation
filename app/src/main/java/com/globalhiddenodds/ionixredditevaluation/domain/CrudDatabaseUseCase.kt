package com.globalhiddenodds.ionixredditevaluation.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import com.globalhiddenodds.ionixredditevaluation.datasource.database.data.Posting
import com.globalhiddenodds.ionixredditevaluation.di.IoDispatcher
import com.globalhiddenodds.ionixredditevaluation.repository.PostingDao
import com.globalhiddenodds.ionixredditevaluation.workers.DownNewsWorker
import com.globalhiddenodds.ionixredditevaluation.workers.FilterNewsWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Pattern adapter
// Pattern observer
class CrudDatabaseUseCase @Inject constructor(
    private val postingDao: PostingDao,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher
) {
    val listPosting: LiveData<List<Posting>> = postingDao
        .getPosting().asLiveData().distinctUntilChanged()

    suspend fun cleanDatabase(): Result<Boolean> {
        return withContext(ioDispatcher) {
            postingDao.deletePostings()
            return@withContext Result.success(true)
        }
    }

    suspend fun insertPosting(): Result<Boolean> {
        return withContext(ioDispatcher) {
            val list = FilterNewsWorker.listPostingCloud
            when {
                list.isNotEmpty() -> {
                    list.forEach {
                        postingDao.insert(it.toPosting())
                    }
                    FilterNewsWorker.listPostingCloud = listOf()
                    DownNewsWorker.mapNews.clear()
                    return@withContext Result.success(true)
                }
                else -> {
                    return@withContext Result.success(false)
                }
            }
        }
    }
}