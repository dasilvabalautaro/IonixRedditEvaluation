package com.globalhiddenodds.ionixredditevaluation.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.datasource.network.NewsCloud
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Pattern adapter
class DownNewsWorker @Inject constructor(
    @ApplicationContext val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Suppress("UNCHECKED_CAST")
    override suspend fun doWork(): Result {
        makeStatusNotification(
            context.getString(R.string.lbl_down_news),
            applicationContext
        )

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val responseNews = NewsCloud
                    .retrofitService
                    .getNews()
                mapNews = responseNews as MutableMap<String?, Any>
                val isSuccess = true
                val outputData: Data = workDataOf(KEY_IS_SUCCESS to isSuccess)
                Result.success(outputData)
            } catch (throwable: Throwable) {
                Result.failure()
            }
        }
    }

    companion object {
        var mapNews = mutableMapOf<String?, Any>()
    }
}