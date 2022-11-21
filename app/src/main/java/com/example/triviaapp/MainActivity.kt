package com.example.triviaapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.triviaapp.domain.model.QuestionsResponse
import com.example.triviaapp.domain.model.QuestionsResponseItem
import com.example.triviaapp.ui.component.LoadingCircular
import com.example.triviaapp.ui.theme.TriviaAppTheme
import com.example.triviaapp.ui.theme.slightlyDeemphasizedAlpha
import com.example.triviaapp.ui.theme.stronglyDeemphasizedAlpha
import com.example.triviaapp.utils.Response
import com.example.triviaapp.viewModel.HomeViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

private const val INITIAL_SECONDS = 0f
private const val MAXIMUM_SECONDS = 1f
private const val DELAY_MILLISECONDS = 50L

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriviaAppTheme {
                val homeViewModel: HomeViewModel = hiltViewModel()

                when (val questionResponse = homeViewModel.questionsState) {
                    is Response.Loading -> {
                        LoadingCircular(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    is Response.Success -> {
                        questionResponse
                            .data
                            ?.firstOrNull()
                            ?.let { questionsResponseItem ->
                                HomeScreen(
                                    questionsResponseItem,
                                    onSelectedAnswer = { answer ->

                                        Toast.makeText(
                                            applicationContext,
                                            (questionsResponseItem.correctAnswer == answer).toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        /*homeViewModel.checkAnswer(
                                            correctAnswer = questionsResponseItem.correctAnswer,
                                            selectedAnswer = answer
                                        )*/
                                    },
                                    onRefresh = {
                                        homeViewModel.getQuestions()
                                    }
                                )
                            }
                            ?: Toast.makeText(
                                applicationContext, "ERROR", Toast.LENGTH_LONG
                            ).show()
                    }
                    is Response.Failure -> {

                    }
                }

//                when (homeViewModel.selectionStatus) {
//                    true -> {
//                        Toast.makeText(applicationContext, "SIIIIII!!", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    false -> {
//                        Toast.makeText(applicationContext, "NOO!!!", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun HomeScreen(
    questionResponse: QuestionsResponseItem,
    onSelectedAnswer: (answer: String) -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Top App Bar")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Menu, null)
                    }
                },
                actions = {
                    IconButton(onClick = { onRefresh() }) {
                        Icon(Icons.Filled.Refresh, null)
                    }
                }
            )
        },
        floatingActionButton = {},
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                TimerProgress()

                Spacer(modifier = Modifier.height(5.dp))

                QuestionsContent(questionResponse, onSelectedAnswer)
            }

        },
        bottomBar = {
            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    )
}

@Composable
fun TimerProgress() {
    var ticks by rememberSaveable { mutableStateOf(INITIAL_SECONDS) }

    LaunchedEffect(Unit) {
        while (ticks < MAXIMUM_SECONDS) {
            delay(DELAY_MILLISECONDS)
            ticks += 0.01f
        }
    }

    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        progress = ticks
    )
}

@Composable
fun QuestionsContent(
    questionResponseItem: QuestionsResponseItem,
    onSelectedAnswer: (answer: String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .fillMaxHeight()
    ) {
        with(questionResponseItem) {
            QuestionTitle(question)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.select_your_answer),
                color = MaterialTheme.colorScheme.onSurface
                    .copy(alpha = stronglyDeemphasizedAlpha),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp, start = 8.dp, end = 8.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            ListOfAnswer(incorrectAnswers, correctAnswer, onSelectedAnswer)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ListOfAnswer(
    incorrectAnswers: List<String>,
    correctAnswer: String,
    onSelectedAnswer: (answer: String) -> Unit
) {
    val selectedValue = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 5.dp)
    ) {
        val answers = mutableListOf<String>().apply {
            addAll(incorrectAnswers)
            add(correctAnswer)
            shuffle()
        }

        items(answers) { answer ->
            AnswerItem(answer, selectedValue, onSelectedAnswer)
        }
    }
}

@Composable
fun AnswerItem(
    answer: String,
    selectedValue: MutableState<String>,
    onSelectedAnswer: (answer: String) -> Unit
) {
    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
    val onChangeState: (String) -> Unit = {
        selectedValue.value = it
        onSelectedAnswer(it)
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = 1.2.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = isSelectedItem(answer),
                    onClick = { onChangeState(answer) }
                )
                .padding(vertical = 20.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = answer,
                maxLines = 3,
                modifier = Modifier.weight(1f),
            )
            RadioButton(
                selected = isSelectedItem(answer),
                onClick = { onChangeState(answer) }
            )
        }
    }
}

@Composable
fun QuestionTitle(questionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = questionText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = slightlyDeemphasizedAlpha),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val jsonResponse = """
        [
          {
            "category": "Music",
            "id": "6250644ee12f6dec240bdfa9",
            "correctAnswer": "Rockwell",
            "incorrectAnswers": [
              "The Verve aaaaaaaaa bbbb ccccc ddddd eeeeee fffffff ggg hhhhhhhh",
              "Big Country",
              "Deee-Lite"
            ],
            "question": "'Somebody's Watching Me' was a one hit wonder in 1984 by which artist?",
            "tags": [
              "songs",
              "one_hit_wonders",
              "music"
            ],
            "type": "Multiple Choice",
            "difficulty": "hard",
            "regions": [ ]
          }
        ]
    """.trimIndent()

    TriviaAppTheme {
        val questionsResponse = Gson().fromJson(jsonResponse, QuestionsResponse::class.java)
        HomeScreen(questionsResponse.first(), onSelectedAnswer = {}, onRefresh = {})
    }
}