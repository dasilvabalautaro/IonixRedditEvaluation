package com.globalhiddenodds.ionixredditevaluation.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.*
import com.globalhiddenodds.ionixredditevaluation.workers.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Pattern mediator
class GetNewsPostingUseCase @Inject constructor(
    @ApplicationContext val context: Context) {
    private val workManager = WorkManager.getInstance(context)
    val workInfo: LiveData<List<WorkInfo>> = workManager
        .getWorkInfosByTagLiveData(TAG_FILTER)

    fun downPosting(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val filterNewsRequest = OneTimeWorkRequest
            .Builder(FilterNewsWorker::class.java)
            .setConstraints(constraints)
            .addTag(TAG_FILTER)
            .build()
        workManager.beginUniqueWork(
            DOWNLOAD_POSTS_WORK_NAME, ExistingWorkPolicy.KEEP,
            OneTimeWorkRequest.Builder(DownNewsWorker::class.java)
                .setConstraints(constraints)
                .addTag(TAG_DOWN)
                .build()).then(filterNewsRequest).enqueue()
    }

    internal fun cancelWork(){
        workManager.cancelUniqueWork(DOWNLOAD_POSTS_WORK_NAME)
    }
}