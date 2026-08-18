package com.georgevik.nqueens.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgevik.nqueens.ui.navigation.RootNav

@Composable
fun MainScreen() {
    Scaffold { innerValues ->
        RootNav(modifier = Modifier.fillMaxSize().padding(innerValues))

    }
}
