package com.globalhiddenodds.ionixredditevaluation

import com.globalhiddenodds.ionixredditevaluation.datasource.network.NewsCloudService
import com.globalhiddenodds.ionixredditevaluation.workers.getChildCloud
import com.globalhiddenodds.ionixredditevaluation.workers.getPosting
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class CloudServiceTest {
    @get:Rule
    var rule = OkHttpIdlingResourceRule()

    private val mockWebServer = MockWebServer()
    lateinit var cloudService: NewsCloudService
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Before
    fun setup() {
        mockWebServer.start()
        cloudService = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(NewsCloudService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun shouldShowResponseCorrectly() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(FileReader.readStringFromFile("success_response.json"))
        )
        val response = cloudService.getNews("")

        @Suppress("UNCHECKED_CAST")
        val jsonMap: MutableMap<String?, Any> = response as MutableMap<String?, Any>
        val child = getChildCloud(jsonMap)
        val listPost = getPosting(child)
        assert(listPost.isNotEmpty())
    }

    @Test
    fun shouldShowError() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(response)
        assert(!response.status.contains("200"))
    }
}