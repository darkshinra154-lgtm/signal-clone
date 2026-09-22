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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Badge
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.data.model.Channel
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.components.AppMenuButton
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary
import com.example.signalclone.ui.theme.TextTertiary

@Composable
fun ChatsTab(
    onSelectChannel: (String) -> Unit,
    onNavigateToNewMessage: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onSignOut: () -> Unit
) {
    val currentUser by SignalRepository.currentUser.collectAsState()
    val channels by SignalRepository.channels.collectAsState()

    Scaffold(
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
                    onSignOut = onSignOut,
                    modifier = Modifier.testTag("app_menu_button")
                )

                Text(
                    text = "Signal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onNavigateToNewMessage,
                        modifier = Modifier.testTag("new_message_header_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "New Message",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNewMessage,
                containerColor = SignalBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_new_message")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Compose",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (channels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No chats yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap the compose button to start messaging",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(channels, key = { it.id }) { channel ->
                    ChannelListItem(
                        channel = channel,
                        onClick = { onSelectChannel(channel.id) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 76.dp),
                        color = BorderLight.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun ChannelListItem(
    channel: Channel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("channel_item_${channel.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarView(
            name = channel.name,
            avatarUrl = channel.avatarUrl,
            isGroup = channel.isGroup,
            size = 50.dp,
            isOnline = channel.isOnline
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = channel.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Text(
                    text = channel.lastMessageTime,
                    fontSize = 12.sp,
                    color = if (channel.unreadCount > 0) SignalBlue else TextTertiary,
                    fontWeight = if (channel.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = channel.lastMessage.ifEmpty { "Start a conversation" },
                    fontSize = 14.sp,
                    color = if (channel.unreadCount > 0) TextPrimary else TextSecondary,
                    fontWeight = if (channel.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (channel.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SignalBlue)
                            .padding(horizontal = 7.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = channel.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
