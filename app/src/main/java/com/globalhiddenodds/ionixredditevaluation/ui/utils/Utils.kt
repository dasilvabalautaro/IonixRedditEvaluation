package com.globalhiddenodds.ionixredditevaluation.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.io.ByteArrayOutputStream

// Pattern singleton
object Utils {
    fun notify(context: Context, message: String){
        val appContext = context.applicationContext ?: return
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
    }

    fun encodeImage(bitmap: Bitmap): String? {
        val arrayOutput = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutput)
        val byteArray = arrayOutput.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeBase64(base64: String): Bitmap {
        val imageBytes = Base64.decode(base64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun isConnect(context: Context): Boolean {
        try {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } catch (e: Exception){
            return false
        }
    }
}