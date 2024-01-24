package com.paytongunnell.workouttracker2.navigation

sealed class Direction(
    val path: String
) {
    data object LoadingTestScreen: Direction("loading_test_screen")
    data object HomeTestScreen: Direction("home_test_screen")
    data object SignInTestScreen: Direction("sign_in_test_screen")
}