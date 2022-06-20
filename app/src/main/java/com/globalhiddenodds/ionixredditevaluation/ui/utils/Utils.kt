package com.globalhiddenodds.ionixredditevaluation.ui.utils

import android.content.Context
import android.widget.Toast

// Pattern singleton
object Utils {
    fun notify(context: Context, message: String){
        val appContext = context.applicationContext ?: return
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
    }
}