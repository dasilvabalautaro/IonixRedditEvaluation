@file:JvmName("Constants")
package com.globalhiddenodds.ionixredditevaluation.workers

import com.globalhiddenodds.ionixredditevaluation.datasource.network.data.PostingCloud

@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
@JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1
const val KEY_LIST = "KEY_LIST"
const val KEY_IS_SUCCESS = "IS_SUCCESS"
const val KEY_SIZE_LIST = "KEY_SIZE"
const val DOWNLOAD_POSTS_WORK_NAME = "download_work"
const val TAG_OUTPUT = "OUTPUT"
const val KEY_DB_EXIST = "DB_EXIST"
const val KEY_DATA = "data"
const val KEY_CHILD = "children"
const val KEY_LINK_FLAIR = "link_flair_text"
const val KEY_POST = "post_hint"
const val KEY_ID_POST = "id"
const val KEY_TITLE = "title"
const val KEY_URL = "url"
const val KEY_SCORE = "score"
const val KEY_COMMENTS = "num_comments"
const val VALUE_POSTING = "Shitposting"
const val VALUE_IMAGE = "image"
