package com.globalhiddenodds.ionixredditevaluation.workers

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.ui.utils.Utils
import com.globalhiddenodds.ionixredditevaluation.workers.FilterNewsWorker.Companion.listPostingCloud
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Pattern adapter
class Image64Worker @Inject constructor(
    @ApplicationContext val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        makeStatusNotification(
            context.getString(R.string.lbl_url_to_image),
            applicationContext
        )

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val isSuccess = inputData.getBoolean(KEY_IS_SUCCESS, false)
                if (isSuccess) {
                    for (idx in listPostingCloud.indices) {
                        val base64 = urlToBase64(listPostingCloud[idx].url)
                        if (base64 != null) {
                            listPostingCloud[idx].base64 = base64
                        }
                    }
                    val outputData: Data = workDataOf(KEY_IS_SUCCESS to isSuccess)
                    Result.success(outputData)
                } else {
                    Result.failure()
                }

            } catch (throwable: Throwable) {
                Result.failure()
            }
        }
    }

    private suspend fun urlToBase64(url: String): String? {
        val loader = ImageLoader(applicationContext)
        val request = ImageRequest.Builder(applicationContext)
            .data(url)
            .allowHardware(false)
            .build()
        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        return Utils.encodeImage(bitmap)
    }
}