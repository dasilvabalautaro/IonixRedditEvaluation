package com.globalhiddenodds.ionixredditevaluation

import androidx.test.platform.app.InstrumentationRegistry
import com.globalhiddenodds.ionixredditevaluation.IonixRedditApplication
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

object FileReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = (InstrumentationRegistry
                .getInstrumentation().targetContext
                .applicationContext as IonixRedditApplication)
                .assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        }catch (e: IOException){
            throw e
        }
    }
}