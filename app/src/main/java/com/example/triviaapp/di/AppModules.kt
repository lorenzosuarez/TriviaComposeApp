package com.example.triviaapp.di

import com.example.triviaapp.domain.repository.QuestionsRepository
import com.example.triviaapp.network.repository.QuestionsRepositoryImpl
import com.example.triviaapp.network.service.QuestionsService
import com.example.triviaapp.utils.Const.WEB_API
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

/**
 * Created by Lorenzo on 11/20/2022.
 */

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    @Provides
    @Named("WEB_API")
    fun provideWebAPI(): String = WEB_API

    @Provides
    fun provideRetrofit(
        @Named("WEB_API") webAPI: String,
    ): Retrofit {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
        return Retrofit.Builder()
            .baseUrl(webAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideQuestionService(
        retrofit: Retrofit
    ): QuestionsService = retrofit.create(QuestionsService::class.java)

    @Provides
    fun provideQuestionsRepository(
        questionsService: QuestionsService
    ): QuestionsRepository = QuestionsRepositoryImpl(
        questionsService = questionsService
    )
}