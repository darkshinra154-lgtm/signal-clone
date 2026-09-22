package com.example.signalclone.ui.screens.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.signalclone.ui.theme.SignalBlueLight
import com.example.signalclone.ui.theme.SurfaceLight
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary
import com.example.signalclone.ui.theme.TextTertiary
import kotlinx.coroutines.launch

@Composable
fun ChatsTab(
    onSelectChannel: (String) -> Unit,
    onNavigateToNewMessage: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onSignOut: () -> Unit
) {
    val currentUser by SignalRepository.currentUser.collectAsState()
    val channels by SignalRepository.channels.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val filteredChannels = remember(channels, searchQuery, selectedFilter) {
        channels.filter { channel ->
            val matchesSearch = if (searchQuery.isBlank()) true else {
                channel.name.contains(searchQuery, ignoreCase = true) ||
                channel.lastMessage.contains(searchQuery, ignoreCase = true)
            }
            val matchesFilter = when (selectedFilter) {
                "Unread" -> channel.unreadCount > 0
                "Direct" -> !channel.isGroup && !channel.isSelfNote
                "Groups" -> channel.isGroup
                "Pinned" -> channel.isPinned
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppMenuButton(
                        user = currentUser,
                        onNavigateToProfile = onNavigateToProfile,
                        onSignOut = onSignOut,
                        modifier = Modifier.testTag("app_menu_button")
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Adam Signal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified by Adam Dev",
                            tint = SignalBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        IconButton(
                            onClick = { isSearchActive = !isSearchActive },
                            modifier = Modifier.testTag("toggle_search_button")
                        ) {
                            Icon(
                                imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

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

                // Search Bar animated
                AnimatedVisibility(
                    visible = isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search chats or messages...", fontSize = 14.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = TextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SignalBlue,
                                unfocusedBorderColor = BorderLight,
                                focusedContainerColor = SurfaceLight,
                                unfocusedContainerColor = SurfaceLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("chat_search_field")
                        )
                    }
                }

                // Filter Categories Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("All", "Pinned", "Unread", "Direct", "Groups")
                    items(filters) { filter ->
                        val isSelected = selectedFilter == filter
                        ElevatedFilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    text = filter,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.elevatedFilterChipColors(
                                selectedContainerColor = SignalBlue,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceLight,
                                labelColor = TextSecondary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                HorizontalDivider(
                    color = BorderLight.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
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
        if (filteredChannels.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No conversations found",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "Try a different search term" else "Tap compose to start an encrypted chat",
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
                items(filteredChannels, key = { it.id }) { channel ->
                    ChannelListItem(
                        channel = channel,
                        onClick = { onSelectChannel(channel.id) },
                        onTogglePin = {
                            SignalRepository.togglePinChannel(channel.id)
                            scope.launch {
                                val action = if (channel.isPinned) "Unpinned" else "Pinned to top 📌"
                                snackbarHostState.showSnackbar("${channel.name}: $action")
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 76.dp),
                        color = BorderLight.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

@Composable
fun ChannelListItem(
    channel: Channel,
    onClick: () -> Unit,
    onTogglePin: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(if (channel.isSelfNote) SignalBlueLight.copy(alpha = 0.35f) else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("channel_item_${channel.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarView(
            name = channel.name,
            avatarUrl = channel.avatarUrl,
            isGroup = channel.isGroup,
            isSelfNote = channel.isSelfNote,
            size = 52.dp,
            isOnline = channel.isOnline
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = channel.name,
                        fontSize = 16.sp,
                        fontWeight = if (channel.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (channel.isPinned) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = "Pinned",
                            tint = SignalBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = channel.lastMessageTime,
                    fontSize = 12.sp,
                    color = if (channel.unreadCount > 0) SignalBlue else TextTertiary,
                    fontWeight = if (channel.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (channel.isTyping) {
                    Text(
                        text = "${channel.typingUser ?: "Contact"} is typing... 💬",
                        fontSize = 14.sp,
                        color = SignalBlue,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                } else {
                    Text(
                        text = channel.lastMessage.ifEmpty { "Start an encrypted conversation" },
                        fontSize = 14.sp,
                        color = if (channel.unreadCount > 0) TextPrimary else TextSecondary,
                        fontWeight = if (channel.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

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

        // Quick Pin toggle icon
        IconButton(
            onClick = onTogglePin,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PushPin,
                contentDescription = if (channel.isPinned) "Unpin" else "Pin",
                tint = if (channel.isPinned) SignalBlue else TextTertiary.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
