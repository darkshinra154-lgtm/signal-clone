package com.example.signalclone.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.data.model.CallRecord
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.components.AppMenuButton
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.HangupRed
import com.example.signalclone.ui.theme.OnlineGreen
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.SurfaceLight
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun CallsTab(
    onStartCall: (contactName: String, isVideo: Boolean) -> Unit,
    onNavigateToProfile: () -> Unit,
    onSignOut: () -> Unit
) {
    val currentUser by SignalRepository.currentUser.collectAsState()
    val calls by SignalRepository.calls.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppMenuButton(
                    user = currentUser,
                    onNavigateToProfile = onNavigateToProfile,
                    onSignOut = onSignOut
                )

                Text(
                    text = "Calls",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                IconButton(
                    onClick = {
                        onStartCall("Sarah Connor", false)
                    },
                    modifier = Modifier.testTag("dial_new_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "New Call",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onStartCall("Sarah Connor", false)
                },
                containerColor = SignalBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_new_call")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Start Call",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // "Create a Call Link" action
            Surface(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Call link copied to clipboard: https://signal.call/join/sxgx99")
                    }
                },
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_call_link_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(SurfaceLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Call Link",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Create a Call Link",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Share a link for your Signal call",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(start = 76.dp),
                color = BorderLight
            )

            if (calls.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No recent calls",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Get started by calling a friend",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                Text(
                    text = "Recent",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(calls, key = { it.id }) { call ->
                        CallItemRow(
                            call = call,
                            onCallback = {
                                onStartCall(call.contactName, call.isVideo)
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 76.dp),
                            color = BorderLight.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CallItemRow(
    call: CallRecord,
    onCallback: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCallback() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("call_item_${call.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarView(
            name = call.contactName,
            avatarUrl = call.contactAvatar,
            size = 46.dp
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = call.contactName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (call.isMissed) HangupRed else TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when {
                        call.isMissed -> Icons.AutoMirrored.Filled.CallMissed
                        call.isIncoming -> Icons.AutoMirrored.Filled.CallReceived
                        else -> Icons.AutoMirrored.Filled.CallMade
                    },
                    contentDescription = null,
                    tint = if (call.isMissed) HangupRed else OnlineGreen,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "${call.timestamp} • ${call.duration}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }

        IconButton(
            onClick = onCallback,
            modifier = Modifier.testTag("callback_button_${call.id}")
        ) {
            Icon(
                imageVector = if (call.isVideo) Icons.Default.Videocam else Icons.Default.Phone,
                contentDescription = if (call.isVideo) "Video Call" else "Audio Call",
                tint = SignalBlue,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
