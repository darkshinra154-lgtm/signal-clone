package com.example.signalclone.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.localization.LocalizationManager
import com.example.signalclone.ui.screens.tabs.CallsTab
import com.example.signalclone.ui.screens.tabs.ChatsTab
import com.example.signalclone.ui.screens.tabs.StoriesTab
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.TextSecondary

@Composable
fun MainScreen(
    onSelectChannel: (String) -> Unit,
    onNavigateToNewMessage: () -> Unit,
    onStartCall: (contactName: String, isVideo: Boolean) -> Unit,
    onNavigateToProfile: () -> Unit,
    onSignOut: () -> Unit,
    onNavigateToSettings: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                // Chats Tab
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ChatBubble,
                            contentDescription = "Chats"
                        )
                    },
                    label = {
                        Text(
                            text = LocalizationManager.getString("chats"),
                            fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SignalBlue,
                        selectedTextColor = SignalBlue,
                        indicatorColor = SignalBlue.copy(alpha = 0.12f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_chats")
                )

                // Calls Tab
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Calls"
                        )
                    },
                    label = {
                        Text(
                            text = LocalizationManager.getString("calls"),
                            fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SignalBlue,
                        selectedTextColor = SignalBlue,
                        indicatorColor = SignalBlue.copy(alpha = 0.12f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_calls")
                )

                // Stories / Updates Tab
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesomeMotion,
                            contentDescription = "Stories"
                        )
                    },
                    label = {
                        Text(
                            text = LocalizationManager.getString("stories"),
                            fontWeight = if (selectedTab == 2) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SignalBlue,
                        selectedTextColor = SignalBlue,
                        indicatorColor = SignalBlue.copy(alpha = 0.12f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_stories")
                )

                // WhatsApp-Style Settings Tab
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    },
                    label = {
                        Text(
                            text = LocalizationManager.getString("settings"),
                            fontWeight = if (selectedTab == 3) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SignalBlue,
                        selectedTextColor = SignalBlue,
                        indicatorColor = SignalBlue.copy(alpha = 0.12f),
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("tab_settings")
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> ChatsTab(
                    onSelectChannel = onSelectChannel,
                    onNavigateToNewMessage = onNavigateToNewMessage,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToSettings = { selectedTab = 3 },
                    onSignOut = onSignOut
                )
                1 -> CallsTab(
                    onStartCall = onStartCall,
                    onNavigateToProfile = onNavigateToProfile,
                    onSignOut = onSignOut
                )
                2 -> StoriesTab(
                    onNavigateToProfile = onNavigateToProfile,
                    onSignOut = onSignOut
                )
                3 -> WhatsAppSettingsScreen(
                    onNavigateBack = { selectedTab = 0 },
                    onNavigateToEditProfile = onNavigateToProfile,
                    onSelectChannel = onSelectChannel,
                    onSignOut = onSignOut
                )
            }
        }
    }
}
