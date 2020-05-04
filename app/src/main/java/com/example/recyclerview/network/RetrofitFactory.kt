package com.example.recyclerview.network

import com.example.recyclerview.BuildConfig
import com.example.recyclerview.WEATHER_API_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory{

    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url()
            .newBuilder()
            .addQueryParameter("appid", WEATHER_API_KEY)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val loggingInterceptor =  HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //Not logging the authkey if not debug
    private val client =
        if(BuildConfig.DEBUG){
            OkHttpClient().newBuilder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        }else{
            OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()
        }

    fun retrofit(baseUrl : String) : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

}