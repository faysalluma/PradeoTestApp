package com.groupec.cleanarchitecturesampleapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.groupec.cleanarchitecturesampleapp.R
import com.groupec.cleanarchitecturesampleapp.core.designsystem.SampleTopAppBar
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.CustomSnackBar
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.ErrorScreen
import com.groupec.cleanarchitecturesampleapp.core.designsystem.component.SnackbarVisualsWithState
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.Green
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.Red
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.White
import com.groupec.cleanarchitecturesampleapp.navigation.AppNavHost
import com.groupec.cleanarchitecturesampleapp.navigation.NavigationItem


@Composable
fun MainScreen(
    connectionState: Boolean,
    navController: NavHostController = rememberNavController()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val appBarTitle = stringResource(id = R.string.app_name)

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    val isError = (data.visuals as? SnackbarVisualsWithState)?.isError ?: false
                    val containerColor = if (isError) Red else Green

                    CustomSnackBar(
                        data = data,
                        containerColor = containerColor,
                        contentColor = White
                    )
                }
            )
        },
        topBar = {
            SampleTopAppBar(
                appBarTitle
            )
        }
    ) {
        if (!connectionState) {
            ErrorScreen(error = "No internet connexion")
        } else {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AppNavHost(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}


