package com.globalhiddenodds.ionixredditevaluation.datasource.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val baseUrl = "https://www.reddit.com/r/chile"

//new/.json?limit=1000
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface NewsCloudService {
    @GET("new/{param}")
    suspend fun getNews(@Path("param") param: String): Any
}

object NewsCloud {
    val retrofitService: NewsCloudService by lazy {
        retrofit.create(NewsCloudService::class.java)
    }
}