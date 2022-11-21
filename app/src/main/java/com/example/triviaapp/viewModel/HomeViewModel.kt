package com.example.triviaapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.domain.model.QuestionsResponse
import com.example.triviaapp.domain.repository.QuestionsRepository
import com.example.triviaapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Lorenzo on 11/20/2022.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository
) : ViewModel() {
    var questionsState by mutableStateOf<Response<QuestionsResponse>>(Response.Success(null))
        private set

    var selectionStatus by mutableStateOf(false)
        private set

    init {
        getQuestions()
    }

    fun checkAnswer(correctAnswer: String, selectedAnswer: String) {
        selectionStatus = correctAnswer == selectedAnswer
    }

    fun getQuestions() {
        viewModelScope.launch {
            questionsRepository.getQuestions().collect { response ->
                questionsState = response
            }
        }
    }
}