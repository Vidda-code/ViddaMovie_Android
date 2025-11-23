package com.example.viddamovie.di

import com.example.viddamovie.data.remote.TmdbApiService
import com.example.viddamovie.data.remote.YoutubeApiService
import com.example.viddamovie.data.repository.ApiConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()  // Be lenient with malformed JSON
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Logging interceptor - shows network calls in Logcat
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Log all requests/responses
            .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)     // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // Write timeout
            .build()
    }

    @Provides
    @Singleton
    @TmdbRetrofit
    fun provideTmdbRetrofit(
        apiConfig: ApiConfig,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiConfig.tmdbBaseURL)  // https://api.themoviedb.org
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @YoutubeRetrofit
    fun provideYoutubeRetrofit(
        apiConfig: ApiConfig,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiConfig.youtubeSearchURL)  // https://www.googleapis.com/youtube/v3/
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideTmdbApiService(
        @TmdbRetrofit retrofit: Retrofit
    ): TmdbApiService {
        return retrofit.create(TmdbApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideYoutubeApiService(
        @YoutubeRetrofit retrofit: Retrofit
    ): YoutubeApiService {
        return retrofit.create(YoutubeApiService::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YoutubeRetrofit
