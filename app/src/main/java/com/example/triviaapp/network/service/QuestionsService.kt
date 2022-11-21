package com.example.triviaapp.network.service

import com.example.triviaapp.domain.model.QuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Lorenzo on 11/20/2022.
 */

interface QuestionsService {
    @GET("questions")
    suspend fun getAllQuestions(
        @Query("categories") categories: String = "music",
        @Query("limit") limit: Int = 1,
    ) : QuestionsResponse
}