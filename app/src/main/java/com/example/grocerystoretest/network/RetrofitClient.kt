package com.example.grocerystoretest.network

import android.content.Context
import com.example.grocerystoretest.utils.ApplicationPreference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://driving-sweeping-joey.ngrok-free.app" + "/api/"
    private const val TIME_OUT: Long = 10
    private const val LANGUAGE_VI: String = "vi"

    fun getInstance(context: Context): ApiService {
        val accessToken = ApplicationPreference.getInstance(context)?.getAccessToken()

        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        httpClient.readTimeout(TIME_OUT, TimeUnit.SECONDS)

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Accept-Language", LANGUAGE_VI)
                .build()
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(ApiService::class.java)
    }
}