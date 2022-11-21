package com.example.triviaapp.utils

import com.example.triviaapp.utils.Const.HOME_SCREEN

sealed class Route(val route: String) {
    object Home: Route(HOME_SCREEN)
}