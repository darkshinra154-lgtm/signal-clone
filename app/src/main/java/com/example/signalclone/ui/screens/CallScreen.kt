package com.example.signalclone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.DarkCallBackground
import com.example.signalclone.ui.theme.DarkCallControl
import com.example.signalclone.ui.theme.HangupRed
import com.example.signalclone.ui.theme.OnlineGreen
import com.example.signalclone.ui.theme.SignalBlue
import kotlinx.coroutines.delay

@Composable
fun CallScreen(
    contactName: String,
    initialIsVideo: Boolean = false,
    onEndCall: () -> Unit
) {
    var isVideoEnabled by remember { mutableStateOf(initialIsVideo) }
    var isMuted by remember { mutableStateOf(false) }
    var isFrontCamera by remember { mutableStateOf(true) }
    var callSeconds by remember { mutableIntStateOf(0) }
    var isConnected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        isConnected = true
        while (true) {
            delay(1000)
            callSeconds++
        }
    }

    val formattedDuration = remember(callSeconds) {
        val mins = callSeconds / 60
        val secs = callSeconds % 60
        String.format("%02d:%02d", mins, secs)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCallBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Video Preview Simulation
        if (isVideoEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AvatarView(
                        name = contactName,
                        size = 110.dp,
                        fontSize = 38
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isFrontCamera) "Live HD Video (Front Camera)" else "Live HD Video (Rear Camera)",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Top info header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isVideoEnabled) {
                AvatarView(
                    name = contactName,
                    size = 100.dp,
                    fontSize = 36
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            Text(
                text = contactName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isConnected) formattedDuration else "Ringing...",
                fontSize = 16.sp,
                color = if (isConnected) OnlineGreen else Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Encrypted",
                    tint = SignalBlue,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "End-to-end encrypted",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        // Bottom Controls Bar (styled like the React Native stream-io video CallControls)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFF1C1C1E))
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flip camera
                IconButton(
                    onClick = { isFrontCamera = !isFrontCamera },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(DarkCallControl)
                        .testTag("call_flip_camera_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Flip Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Toggle Video
                IconButton(
                    onClick = { isVideoEnabled = !isVideoEnabled },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (isVideoEnabled) DarkCallControl else Color(0xFF4B5563))
                        .testTag("call_toggle_video_button")
                ) {
                    Icon(
                        imageVector = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Toggle Video",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Toggle Mic
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (!isMuted) DarkCallControl else HangupRed.copy(alpha = 0.6f))
                        .testTag("call_toggle_mic_button")
                ) {
                    Icon(
                        imageVector = if (!isMuted) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Toggle Mic",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Hang Up Call
                IconButton(
                    onClick = {
                        SignalRepository.addCallRecord(
                            contactName = contactName,
                            contactAvatar = null,
                            isVideo = isVideoEnabled,
                            isIncoming = false,
                            isMissed = false,
                            duration = if (isConnected) formattedDuration else "0s"
                        )
                        onEndCall()
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(HangupRed)
                        .testTag("call_hangup_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
