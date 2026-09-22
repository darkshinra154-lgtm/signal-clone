package com.example.signalclone.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.signalclone.data.model.Message
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.localization.LocalizationManager
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.BubbleReceiver
import com.example.signalclone.ui.theme.BubbleSender
import com.example.signalclone.ui.theme.OnlineGreen
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.SurfaceLight
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    channelId: String,
    onNavigateBack: () -> Unit,
    onStartCall: (contactName: String, isVideo: Boolean) -> Unit
) {
    val channels by SignalRepository.channels.collectAsState()
    val allMessages by SignalRepository.messages.collectAsState()
    val globalWallpaper by SignalRepository.chatWallpaper.collectAsState()
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()

    val channel = channels.find { it.id == channelId } ?: channels.firstOrNull()
    val messages = allMessages[channelId] ?: emptyList()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var inputText by remember { mutableStateOf("") }
    var replyingToMessage by remember { mutableStateOf<Message?>(null) }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var showSafetyNumberDialog by remember { mutableStateOf(false) }
    var showTimerDialog by remember { mutableStateOf(false) }
    var showWallpaperDialog by remember { mutableStateOf(false) }
    var showClearChatDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var reactionMessageTarget by remember { mutableStateOf<Message?>(null) }
    var activeViewerImage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Determine wallpaper background color based on WhatsApp themes
    val activeWallpaper = channel?.customWallpaper ?: globalWallpaper
    val chatBackgroundColor = when (activeWallpaper) {
        "Teal" -> Color(0xFFE0F2F1)
        "Navy" -> Color(0xFF1B242C)
        "Amber" -> Color(0xFFFFF8E7)
        "Sky" -> Color(0xFFE1F5FE)
        "Classic" -> Color(0xFFEFEAE2) // Classic WhatsApp beige/taupe
        else -> Color(0xFFEFEAE2)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { showSafetyNumberDialog = true }
                            .padding(vertical = 4.dp)
                    ) {
                        AvatarView(
                            name = channel?.name ?: "Chat",
                            avatarUrl = channel?.avatarUrl,
                            isGroup = channel?.isGroup ?: false,
                            isSelfNote = channel?.isSelfNote ?: false,
                            size = 38.dp,
                            isOnline = channel?.isOnline ?: false
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = channel?.name ?: "Chat",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (channel?.isMuted == true) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.VolumeOff,
                                        contentDescription = "Muted",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Encrypted",
                                    tint = SignalBlue,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (channel?.isTyping == true) LocalizationManager.getString("typing_indicator")
                                    else if (channel?.ephemeralTimerSec != null && channel.ephemeralTimerSec > 0) "${LocalizationManager.getString("disappearing_messages")} (${channel.ephemeralTimerSec}s)"
                                    else "Adam Dev • ${LocalizationManager.getString("safety_number")}",
                                    fontSize = 11.sp,
                                    color = if (channel?.isTyping == true) SignalBlue else TextSecondary,
                                    fontWeight = if (channel?.isTyping == true) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("chat_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onStartCall(channel?.name ?: "Call", true) },
                        modifier = Modifier.testTag("chat_video_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Call",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { onStartCall(channel?.name ?: "Call", false) },
                        modifier = Modifier.testTag("chat_audio_call_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Audio Call",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = TextPrimary
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            // Mute / Unmute
                            DropdownMenuItem(
                                text = {
                                    Text(if (channel?.isMuted == true) LocalizationManager.getString("unmute") else LocalizationManager.getString("mute"))
                                },
                                leadingIcon = {
                                    Icon(
                                        if (channel?.isMuted == true) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                        contentDescription = null,
                                        tint = SignalBlue
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    SignalRepository.toggleMuteChannel(channelId)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (channel?.isMuted == true) "Notifications unmuted" else "Notifications muted 🔕"
                                        )
                                    }
                                }
                            )

                            // Wallpaper
                            DropdownMenuItem(
                                text = { Text(LocalizationManager.getString("wallpaper")) },
                                leadingIcon = {
                                    Icon(Icons.Default.Wallpaper, contentDescription = null, tint = SignalBlue)
                                },
                                onClick = {
                                    showMenu = false
                                    showWallpaperDialog = true
                                }
                            )

                            // Disappearing Messages
                            DropdownMenuItem(
                                text = { Text(LocalizationManager.getString("disappearing_messages")) },
                                leadingIcon = {
                                    Icon(Icons.Default.Timer, contentDescription = null, tint = SignalBlue)
                                },
                                onClick = {
                                    showMenu = false
                                    showTimerDialog = true
                                }
                            )

                            // Safety Number
                            DropdownMenuItem(
                                text = { Text(LocalizationManager.getString("safety_number")) },
                                leadingIcon = {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = SignalBlue)
                                },
                                onClick = {
                                    showMenu = false
                                    showSafetyNumberDialog = true
                                }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                            // Clear Chat
                            DropdownMenuItem(
                                text = { Text(LocalizationManager.getString("clear_chat"), color = Color(0xFFDC2626)) },
                                leadingIcon = {
                                    Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = Color(0xFFDC2626))
                                },
                                onClick = {
                                    showMenu = false
                                    showClearChatDialog = true
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = chatBackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Safety Header Card with Adam Dev branding
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .clickable { showSafetyNumberDialog = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = SignalBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Encrypted by Adam Dev Protocol • ${LocalizationManager.getString("safety_number")}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        onLongClick = { reactionMessageTarget = message },
                        onImageClick = { imageUrl -> activeViewerImage = imageUrl }
                    )
                }

                // If contact is typing
                if (channel?.isTyping == true) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(BubbleReceiver)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "${channel.name} ${LocalizationManager.getString("typing_indicator")}",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "💬", fontSize = 14.sp)
                        }
                    }
                }
            }

            // Quick Message Context & Emoji Reaction Bar (WhatsApp Style)
            if (reactionMessageTarget != null) {
                val target = reactionMessageTarget!!
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        // Quick Emoji reactions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf("❤️", "👍", "😂", "😮", "😢", "🙏", "🔥").forEach { emoji ->
                                Text(
                                    text = emoji,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .clickable {
                                            SignalRepository.addReaction(
                                                channelId = channelId,
                                                messageId = target.id,
                                                emoji = emoji
                                            )
                                            reactionMessageTarget = null
                                        }
                                        .padding(4.dp)
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // WhatsApp Action Buttons (Star, Reply, Delete)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Star / Unstar
                            TextButton(
                                onClick = {
                                    SignalRepository.toggleStarMessage(channelId, target.id)
                                    val isStarredNow = !target.isStarred
                                    reactionMessageTarget = null
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (isStarredNow) "Message starred ⭐" else "Message unstarred"
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (target.isStarred) Color(0xFFF59E0B) else TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (target.isStarred) LocalizationManager.getString("unstar") else LocalizationManager.getString("star"),
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            }

                            // Reply
                            TextButton(
                                onClick = {
                                    replyingToMessage = target
                                    reactionMessageTarget = null
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Reply,
                                    contentDescription = null,
                                    tint = SignalBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = LocalizationManager.getString("reply"),
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            }

                            // Delete
                            TextButton(
                                onClick = {
                                    SignalRepository.deleteMessage(channelId, target.id)
                                    reactionMessageTarget = null
                                    scope.launch {
                                        snackbarHostState.showSnackbar(LocalizationManager.getString("message_deleted"))
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = LocalizationManager.getString("delete"),
                                    color = Color(0xFFDC2626),
                                    fontSize = 13.sp
                                )
                            }

                            // Close / Cancel
                            IconButton(onClick = { reactionMessageTarget = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                            }
                        }
                    }
                }
            }

            // Quoted Reply Preview Bar (WhatsApp Style)
            AnimatedVisibility(visible = replyingToMessage != null) {
                if (replyingToMessage != null) {
                    val replyTarget = replyingToMessage!!
                    Surface(
                        color = Color(0xFFF3F4F6),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(SignalBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = replyTarget.senderName.ifBlank { "Contact" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = SignalBlue
                                )
                                Text(
                                    text = replyTarget.text.ifBlank { "[Media]" },
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(
                                onClick = { replyingToMessage = null },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancel reply",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showAttachmentSheet = true },
                    modifier = Modifier.testTag("chat_attach_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Attach",
                        tint = TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text(LocalizationManager.getString("type_message"), color = TextSecondary, fontSize = 15.sp) },
                    shape = RoundedCornerShape(22.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_message_input")
                )

                Spacer(modifier = Modifier.width(6.dp))

                if (inputText.isNotBlank()) {
                    IconButton(
                        onClick = {
                            val text = inputText.trim()
                            if (text.isNotEmpty()) {
                                SignalRepository.sendMessage(
                                    channelId = channelId,
                                    text = text,
                                    replyToSender = replyingToMessage?.senderName,
                                    replyToText = replyingToMessage?.text
                                )
                                inputText = ""
                                replyingToMessage = null
                            }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SignalBlue)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            SignalRepository.sendMessage(
                                channelId = channelId,
                                text = "Encrypted photo",
                                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=500",
                                replyToSender = replyingToMessage?.senderName,
                                replyToText = replyingToMessage?.text
                            )
                            replyingToMessage = null
                        },
                        modifier = Modifier.testTag("chat_camera_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            SignalRepository.sendMessage(
                                channelId = channelId,
                                text = "Encrypted voice note",
                                isAudio = true,
                                audioSec = 14,
                                replyToSender = replyingToMessage?.senderName,
                                replyToText = replyingToMessage?.text
                            )
                            replyingToMessage = null
                        },
                        modifier = Modifier.testTag("chat_mic_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Record Voice",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }

    // Wallpaper Selection Dialog
    if (showWallpaperDialog) {
        val wallpapers = listOf(
            "Classic" to "Classic Wallpaper (WhatsApp Beige)",
            "Teal" to "Emerald Teal (WhatsApp Green)",
            "Navy" to "Dark Midnight Navy",
            "Amber" to "Warm Sunlit Amber",
            "Sky" to "Breezy Sky Blue"
        )
        AlertDialog(
            onDismissRequest = { showWallpaperDialog = false },
            title = {
                Text(LocalizationManager.getString("wallpaper"), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    wallpapers.forEach { (key, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    SignalRepository.setChannelWallpaper(channelId, key)
                                    showWallpaperDialog = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Wallpaper updated to $key ✨")
                                    }
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                fontWeight = if (activeWallpaper == key) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeWallpaper == key) SignalBlue else TextPrimary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWallpaperDialog = false }) {
                    Text(LocalizationManager.getString("cancel"), color = TextSecondary)
                }
            }
        )
    }

    // Clear Chat Confirmation Dialog
    if (showClearChatDialog) {
        AlertDialog(
            onDismissRequest = { showClearChatDialog = false },
            title = {
                Text(LocalizationManager.getString("clear_chat"), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Text(
                    text = "Are you sure you want to clear all messages in this chat? Starred messages will also be cleared.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        SignalRepository.clearChat(channelId)
                        showClearChatDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar(LocalizationManager.getString("chat_cleared"))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text(LocalizationManager.getString("clear_chat"), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearChatDialog = false }) {
                    Text(LocalizationManager.getString("cancel"), color = TextSecondary)
                }
            }
        )
    }

    // Safety Number Verification Dialog
    if (showSafetyNumberDialog) {
        AlertDialog(
            onDismissRequest = { showSafetyNumberDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = SignalBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(LocalizationManager.getString("safety_number"), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Verify that your messages with ${channel?.name} are encrypted end-to-end with the Adam Dev cryptographic ratchet.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    // Simulated QR Code representation
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E293B)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QR Code",
                            tint = Color.White,
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    // 60-digit safety fingerprint
                    Text(
                        text = "38291  49102  84729  01847\n59281  73920  18492  03829\n47192  84910  28491  04829",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSafetyNumberDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Safety Number verified with ${channel?.name}! 🛡️")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SignalBlue)
                ) {
                    Text("Mark as Verified", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSafetyNumberDialog = false }) {
                    Text(LocalizationManager.getString("cancel"), color = TextSecondary)
                }
            }
        )
    }

    // Disappearing Messages Timer Dialog
    if (showTimerDialog) {
        val timerOptions = listOf(
            0 to "Off",
            10 to "10 seconds",
            60 to "1 minute",
            3600 to "1 hour",
            86400 to "1 day",
            604800 to "1 week"
        )
        AlertDialog(
            onDismissRequest = { showTimerDialog = false },
            title = {
                Text(LocalizationManager.getString("disappearing_messages"), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column {
                    Text(
                        text = "Set a timer for messages to disappear from both devices after they've been seen:",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    timerOptions.forEach { (sec, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    SignalRepository.setEphemeralTimer(channelId, sec)
                                    showTimerDialog = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Disappearing messages set to: $label ⏱️")
                                    }
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 15.sp,
                                fontWeight = if ((channel?.ephemeralTimerSec ?: 0) == sec) FontWeight.Bold else FontWeight.Normal,
                                color = if ((channel?.ephemeralTimerSec ?: 0) == sec) SignalBlue else TextPrimary
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTimerDialog = false }) {
                    Text(LocalizationManager.getString("cancel"), color = TextSecondary)
                }
            }
        )
    }

    // Fullscreen Image Viewer
    if (activeViewerImage != null) {
        Dialog(
            onDismissRequest = { activeViewerImage = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = activeViewerImage,
                    contentDescription = "Full photo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                // Top control bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Encrypted Media", color = Color.White, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { activeViewerImage = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
            }
        }
    }

    // Attachment bottom sheet
    if (showAttachmentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAttachmentSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Share Content",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            SignalRepository.sendMessage(
                                channelId = channelId,
                                text = "Photo shared",
                                imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500"
                            )
                            showAttachmentSheet = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(SignalBlue.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Gallery",
                                tint = SignalBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Gallery", fontSize = 13.sp, color = TextPrimary)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            SignalRepository.sendMessage(
                                channelId = channelId,
                                text = "Live snapshot",
                                imageUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=500"
                            )
                            showAttachmentSheet = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(OnlineGreen.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                tint = OnlineGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Camera", fontSize = 13.sp, color = TextPrimary)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            SignalRepository.sendMessage(
                                channelId = channelId,
                                text = "Voice note (18s)",
                                isAudio = true,
                                audioSec = 18
                            )
                            showAttachmentSheet = false
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF7C3AED).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Note",
                                tint = Color(0xFF7C3AED),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Voice Note", fontSize = 13.sp, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: Message,
    onLongClick: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val isMe = message.isSentByMe
    var isPlayingAudio by remember { mutableStateOf(false) }
    var audioProgress by remember { mutableIntStateOf(0) }

    LaunchedEffect(isPlayingAudio) {
        if (isPlayingAudio) {
            val total = message.audioDurationSec.coerceAtLeast(6)
            for (i in 0..total) {
                audioProgress = i
                delay(1000)
            }
            isPlayingAudio = false
            audioProgress = 0
        }
    }

    val timeFormatted = remember(message.timestamp) {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLongClick() }
            .testTag("message_bubble_${message.id}"),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 295.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 4.dp,
                        bottomEnd = if (isMe) 4.dp else 16.dp
                    )
                )
                .background(if (isMe) BubbleSender else BubbleReceiver)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                if (!isMe && message.senderName.isNotBlank()) {
                    Text(
                        text = message.senderName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SignalBlue,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                // WhatsApp Quoted Message Box inside Bubble
                if (message.replyToText != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isMe) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.05f))
                            .padding(start = 8.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (isMe) Color.White else SignalBlue)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = message.replyToSender ?: "Contact",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMe) Color.White else SignalBlue
                                )
                                Text(
                                    text = message.replyToText,
                                    fontSize = 11.sp,
                                    color = if (isMe) Color.White.copy(alpha = 0.85f) else TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                if (message.imageUrl != null) {
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = "Attached photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onImageClick(message.imageUrl) }
                            .padding(bottom = 4.dp)
                    )
                }

                if (message.isAudio) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isMe) Color.White.copy(alpha = 0.25f) else SignalBlue)
                                .clickable { isPlayingAudio = !isPlayingAudio },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlayingAudio) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = "Play voice note",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                val heights = listOf(12, 18, 8, 22, 16, 10, 24, 14, 20, 10, 16, 8)
                                heights.forEachIndexed { idx, h ->
                                    val barHeight = if (isPlayingAudio) (h + (audioProgress * 3 % 10)).coerceIn(6, 26) else h
                                    Box(
                                        modifier = Modifier
                                            .width(3.dp)
                                            .height(barHeight.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(
                                                if (isMe) Color.White.copy(alpha = if (idx < (audioProgress * 2)) 1f else 0.5f)
                                                else SignalBlue.copy(alpha = if (idx < (audioProgress * 2)) 1f else 0.4f)
                                            )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isPlayingAudio) "${audioProgress}s / ${message.audioDurationSec}s" else "${message.audioDurationSec}s",
                                fontSize = 11.sp,
                                color = if (isMe) Color.White.copy(alpha = 0.8f) else TextSecondary
                            )
                        }
                    }
                } else if (message.text.isNotBlank()) {
                    Text(
                        text = message.text,
                        fontSize = 15.sp,
                        color = if (isMe) Color.White else TextPrimary
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (message.isStarred) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Starred",
                            tint = if (isMe) Color(0xFFFBBF24) else Color(0xFFF59E0B),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                    }
                    if (message.expiresInSec > 0) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Expires",
                            tint = if (isMe) Color.White.copy(alpha = 0.7f) else TextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                    }
                    Text(
                        text = timeFormatted,
                        fontSize = 11.sp,
                        color = if (isMe) Color.White.copy(alpha = 0.7f) else TextSecondary
                    )
                    if (isMe) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Delivered",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Reactions badges
        if (message.reactions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 2.dp, start = if (isMe) 0.dp else 4.dp, end = if (isMe) 4.dp else 0.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                message.reactions.forEach { emoji ->
                    Text(text = emoji, fontSize = 13.sp)
                }
            }
        }
    }
}
