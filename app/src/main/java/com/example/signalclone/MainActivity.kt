package com.example.signalclone

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.signalclone.localization.LocalizationManager
import com.example.signalclone.ui.screens.WhatsAppSettingsScreen
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.screens.CallScreen
import com.example.signalclone.ui.screens.ChatDetailScreen
import com.example.signalclone.ui.screens.FindByUsernameScreen
import com.example.signalclone.ui.screens.MainScreen
import com.example.signalclone.ui.screens.NewGroupScreen
import com.example.signalclone.ui.screens.NewMessageScreen
import com.example.signalclone.ui.screens.ProfileScreen
import com.example.signalclone.ui.screens.SignInScreen
import com.example.signalclone.ui.screens.SignUpScreen
import com.example.signalclone.ui.screens.WelcomeScreen
import com.example.signalclone.ui.theme.SignalCloneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignalCloneTheme {
                SignalAppNavigation()
            }
        }
    }
}

@Composable
fun SignalAppNavigation() {
    val navController = rememberNavController()
    val isSignedIn by SignalRepository.isSignedIn.collectAsState()
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()
    val startDestination = if (isSignedIn) "main" else "welcome"

    CompositionLocalProvider(LocalLayoutDirection provides currentLanguage.layoutDirection) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
        composable("welcome") {
            WelcomeScreen(
                onContinue = { navController.navigate("signup") },
                onSignIn = { navController.navigate("signin") }
            )
        }

        composable("signin") {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate("main") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("main") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToSignIn = { navController.navigate("signin") }
            )
        }

        composable("main") {
            MainScreen(
                onSelectChannel = { channelId ->
                    navController.navigate("chat/$channelId")
                },
                onNavigateToNewMessage = {
                    navController.navigate("new_message")
                },
                onStartCall = { contactName, isVideo ->
                    val encodedName = Uri.encode(contactName)
                    navController.navigate("call/$encodedName/$isVideo")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onSignOut = {
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "chat/{channelId}",
            arguments = listOf(navArgument("channelId") { type = NavType.StringType })
        ) { backStackEntry ->
            val channelId = backStackEntry.arguments?.getString("channelId") ?: ""
            ChatDetailScreen(
                channelId = channelId,
                onNavigateBack = { navController.popBackStack() },
                onStartCall = { contactName, isVideo ->
                    val encodedName = Uri.encode(contactName)
                    navController.navigate("call/$encodedName/$isVideo")
                }
            )
        }

        composable(
            route = "call/{contactName}/{isVideo}",
            arguments = listOf(
                navArgument("contactName") { type = NavType.StringType },
                navArgument("isVideo") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val contactName = Uri.decode(backStackEntry.arguments?.getString("contactName") ?: "Call")
            val isVideo = backStackEntry.arguments?.getBoolean("isVideo") ?: false
            CallScreen(
                contactName = contactName,
                initialIsVideo = isVideo,
                onEndCall = { navController.popBackStack() }
            )
        }

        composable("new_message") {
            NewMessageScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNewGroup = { navController.navigate("new_group") },
                onNavigateToFindByUsername = { navController.navigate("find_username") },
                onSelectContact = { channelId ->
                    navController.navigate("chat/$channelId") {
                        popUpTo("main")
                    }
                }
            )
        }

        composable("new_group") {
            NewGroupScreen(
                onNavigateBack = { navController.popBackStack() },
                onGroupCreated = { channelId ->
                    navController.navigate("chat/$channelId") {
                        popUpTo("main")
                    }
                }
            )
        }

        composable("find_username") {
            FindByUsernameScreen(
                onNavigateBack = { navController.popBackStack() },
                onSelectContact = { channelId ->
                    navController.navigate("chat/$channelId") {
                        popUpTo("main")
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignOut = {
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("settings") {
            WhatsAppSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditProfile = { navController.navigate("profile") },
                onSelectChannel = { channelId ->
                    navController.navigate("chat/$channelId")
                },
                onSignOut = {
                    navController.navigate("welcome") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
}
