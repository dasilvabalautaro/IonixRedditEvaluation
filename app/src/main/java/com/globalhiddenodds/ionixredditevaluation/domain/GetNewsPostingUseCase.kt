package com.globalhiddenodds.ionixredditevaluation.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*
import com.globalhiddenodds.ionixredditevaluation.workers.DOWNLOAD_POSTS_WORK_NAME
import com.globalhiddenodds.ionixredditevaluation.workers.DownNewsWorker
import com.globalhiddenodds.ionixredditevaluation.workers.FilterNewsWorker
import com.globalhiddenodds.ionixredditevaluation.workers.TAG_OUTPUT
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Pattern mediator
class GetNewsPostingUseCase @Inject constructor(
    @ApplicationContext val context: Context) {

    private val workManager = WorkManager.getInstance(context)
    val workInfo: LiveData<List<WorkInfo>> = workManager
        .getWorkInfosByTagLiveData(TAG_OUTPUT)

    fun downPosting(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val filterNewsRequest = OneTimeWorkRequest
            .Builder(FilterNewsWorker::class.java)
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        workManager.beginUniqueWork(
            DOWNLOAD_POSTS_WORK_NAME, ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.Builder(DownNewsWorker::class.java)
                .setConstraints(constraints)
                .addTag(TAG_OUTPUT)
                .build()).then(filterNewsRequest).enqueue()
    }

    internal fun cancelWork(){
        workManager.cancelUniqueWork(DOWNLOAD_POSTS_WORK_NAME)
    }
}