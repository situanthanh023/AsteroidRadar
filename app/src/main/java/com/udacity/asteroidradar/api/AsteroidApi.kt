package com.udacity.asteroidradar.api

import android.annotation.SuppressLint
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}

enum class AsteroidApiFilter(val type: String) {
    @SuppressLint("WeekBasedYear", "ConstantLocale")
    SHOW_TODAY(
        SimpleDateFormat(
            Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()
        ).format(
            Calendar.getInstance().time
        ).toString()
    ),
    SHOW_SAVED(""),
    SHOW_ALL("")
}