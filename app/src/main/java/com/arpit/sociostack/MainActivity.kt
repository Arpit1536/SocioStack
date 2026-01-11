package com.arpit.sociostack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.arpit.sociostack.data.local.RolePreferences
import com.arpit.sociostack.ui.theme.SocioStackTheme
import com.arpit.sociostack.views.navigation.AppNavGraph
import com.arpit.sociostack.views.navigation.Screen
import com.arpit.sociostack.vm.RoleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SocioStackTheme {
                val roleViewModel: RoleViewModel = viewModel()
                val role by roleViewModel.role.collectAsState()
                val isLoading by roleViewModel.isLoading.collectAsState()
                val navController = rememberNavController()


                when {
                    isLoading -> {
                        LoadingScreen()
                    }
                    role != null -> {

                        val startDestination = when (role) {
                            "admin" -> Screen.AdminHome.route
                            "member" -> Screen.MemberHome.route
                            else -> Screen.RoleSelection.route
                        }

                        AppNavGraph(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                    else -> {

                        AppNavGraph(
                            navController = navController,
                            startDestination = Screen.RoleSelection.route
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    val gradientColors = listOf(
        Color(0xFF1A0B2E),
        Color(0xFF2D1B69),
        Color(0xFF4A148C)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White
        )
    }
}