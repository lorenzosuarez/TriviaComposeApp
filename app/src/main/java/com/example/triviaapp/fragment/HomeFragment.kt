package com.example.triviaapp.fragment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.triviaapp.viewModel.HomeViewModel


/**
 * Created by Lorenzo on 11/20/2022.
 */

@Composable
fun HomeFragment(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onClickToDetailScreen: (Int) -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val a = 2
//        HomeScreen(
//            modifier = Modifier
//                .padding(
//                    horizontal = 16.dp
//                ),
//            gamesList = homeViewModel.gamesListState.collectAsLazyPagingItems(),
//            onClickToDetailScreen = onClickToDetailScreen,
//        )
    }
}