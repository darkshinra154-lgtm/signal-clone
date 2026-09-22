package com.example.signalclone.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesomeMotion
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.signalclone.ui.screens.tabs.CallsTab
import com.example.signalclone.ui.screens.tabs.ChatsTab
import com.example.signalclone.ui.screens.tabs.StoriesTab
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary

@Composable
fun MainScreen(
    onSelectChannel: (String) -> Unit,
    onNavigateToNewMessage: () -> Unit,
    onStartCall: (contactName: String, isVideo: Boolean) -> Unit,
    onNavigateToProfile: () -> Unit,
    onSignOut: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
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
                            text = "Chats",
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
                            text = "Calls",
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
                            text = "Stories",
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
            }
        }
    }
}
