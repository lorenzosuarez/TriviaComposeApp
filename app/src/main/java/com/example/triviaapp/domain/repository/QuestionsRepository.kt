package com.example.triviaapp.domain.repository

import com.example.triviaapp.domain.model.QuestionsResponse
import com.example.triviaapp.utils.Response
import kotlinx.coroutines.flow.Flow

/**
 * Created by Lorenzo on 11/20/2022.
 */

interface QuestionsRepository {
    fun getQuestions(): Flow<Response<QuestionsResponse>>
}