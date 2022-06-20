package com.globalhiddenodds.ionixredditevaluation.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.datasource.network.data.PostingCloud
import com.globalhiddenodds.ionixredditevaluation.di.IoDispatcher
import com.globalhiddenodds.ionixredditevaluation.workers.DownNewsWorker.Companion.mapNews
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

// Pattern adapter
class FilterNewsWorker @Inject constructor(
    @ApplicationContext val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        makeStatusNotification(context.getString(R.string.lbl_filter_news),
            applicationContext)

        return withContext(Dispatchers.IO){
            return@withContext try {
                val isSuccess = inputData.getBoolean(KEY_IS_SUCCESS, false)
                if (isSuccess){
                    val child = getChildCloud(mapNews)
                    listPostingCloud = getPosting(child)
                    val outputData: Data = workDataOf(KEY_IS_SUCCESS to isSuccess)
                    Result.success(outputData)
                }
                else {
                    Result.failure()
                }
            } catch (throwable: Throwable){
                Result.failure()
            }
        }
    }

    companion object {
        var listPostingCloud = listOf<PostingCloud>()
    }
}