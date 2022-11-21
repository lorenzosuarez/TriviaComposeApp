package com.example.triviaapp.network.repository

import com.example.triviaapp.domain.model.QuestionsResponse
import com.example.triviaapp.domain.repository.QuestionsRepository
import com.example.triviaapp.network.service.QuestionsService
import com.example.triviaapp.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Lorenzo on 11/20/2022.
 */

@Singleton
class QuestionsRepositoryImpl @Inject constructor(
    private val questionsService: QuestionsService
) : QuestionsRepository {
    override fun getQuestions(): Flow<Response<QuestionsResponse>> = flow {
        try {
            emit(Response.Loading)
            val responseApi = questionsService.getAllQuestions()
            emit(Response.Success(responseApi))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}