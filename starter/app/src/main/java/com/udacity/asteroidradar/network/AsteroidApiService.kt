package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.NearEarthObject
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val ASTEROID_BASE_URL = "https://api.nasa.gov/neo/rest/v1/"
private const val NASA_IMAGE_BASE_URL = "https://api.nasa.gov/planetary/"

interface AsteroidApiService {
    @GET("feed")
    suspend fun getNearEarthObject(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("api_key") api_key: String
    ): NearEarthObject
}

interface NasaImageApiService {
    @GET("apod")
    suspend fun getNasaImageOfTheDay(
        @Query("api_key") api_key: String
    ):PictureOfDay
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .readTimeout(30,TimeUnit.SECONDS)
    .connectTimeout(30,TimeUnit.SECONDS)
    .build()

object AsteroidApi {
    val retrofitService = getRetroFit(ASTEROID_BASE_URL).create(AsteroidApiService::class.java)
}

object NasaImageApi {
    val retrofitService = getRetroFit(NASA_IMAGE_BASE_URL).create(NasaImageApiService::class.java)
}

fun getRetroFit(baseUrl:String):Retrofit{
    return  Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .build()
}