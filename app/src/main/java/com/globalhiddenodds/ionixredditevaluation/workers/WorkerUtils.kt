package com.globalhiddenodds.ionixredditevaluation.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.globalhiddenodds.ionixredditevaluation.R
import com.globalhiddenodds.ionixredditevaluation.datasource.network.data.PostingCloud

// Pattern singleton
fun makeStatusNotification(message: String, context: Context) {
    val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
    val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(CHANNEL_ID, name, importance)
    channel.description = description

    val notificationManager =
        context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    NotificationManagerCompat.from(context)
        .notify(NOTIFICATION_ID, builder.build())
}

@Suppress("UNCHECKED_CAST")
fun getChildCloud(map: MutableMap<String?, Any>): ArrayList<Map<String?, Any>>{
    var child = arrayListOf<Map<String?, Any>>()

    map.forEach {
        if(it.key == KEY_DATA){
            val dataMap = it.value as Map<String?, Any>
            for (k in dataMap.keys){
                if (k == KEY_CHILD){
                    child = dataMap[k] as ArrayList<Map<String?, Any>>
                }
            }
        }
    }
    return child
}

@Suppress("UNCHECKED_CAST")
fun getPosting(array: ArrayList<Map<String?, Any>>): MutableList<PostingCloud>{
    val listPosting = mutableListOf<PostingCloud>()
    array.forEach {
        val dataChild = it[KEY_DATA] as Map<String?, Any>
        if (dataChild[KEY_LINK_FLAIR] == VALUE_POSTING &&
                dataChild[KEY_POST] == VALUE_IMAGE){
            val postingCloud = PostingCloud(dataChild[KEY_ID_POST] as String,
                dataChild[KEY_TITLE] as String,
                dataChild[KEY_URL] as String,
                dataChild[KEY_SCORE] as Double,
                dataChild[KEY_COMMENTS] as Double)
            listPosting.add(postingCloud)
        }
    }
    return listPosting
}

@Suppress("UNCHECKED_CAST")
fun getPostHintSearch(array: ArrayList<Map<String?, Any>>): MutableList<PostingCloud>{
    val listPosting = mutableListOf<PostingCloud>()
    array.forEach {
        val dataChild = it[KEY_DATA] as Map<String?, Any>
        if (dataChild[KEY_POST] == VALUE_IMAGE){
            val postingCloud = PostingCloud(dataChild[KEY_ID_POST] as String,
                dataChild[KEY_TITLE] as String,
                dataChild[KEY_URL] as String,
                dataChild[KEY_SCORE] as Double,
                dataChild[KEY_COMMENTS] as Double)
            listPosting.add(postingCloud)
        }
    }
    return listPosting
}