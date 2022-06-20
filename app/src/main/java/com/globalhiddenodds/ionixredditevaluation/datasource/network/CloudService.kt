package com.globalhiddenodds.ionixredditevaluation.datasource.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val baseUrl = "https://www.reddit.com/r/chile/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface NewsCloudService {
    @GET("new/.json?limit=1000")
    suspend fun getNews(): Any
}

// Pattern singleton
object NewsCloud {
    val retrofitService: NewsCloudService by lazy {
        retrofit.create(NewsCloudService::class.java)
    }
}
