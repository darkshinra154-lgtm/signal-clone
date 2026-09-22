package com.example.signalclone.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.signalclone.data.model.Message
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.localization.AppLanguage
import com.example.signalclone.localization.LocalizationManager
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.HangupRed
import com.example.signalclone.ui.theme.OnlineGreen
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.SignalBlueLight
import com.example.signalclone.ui.theme.SurfaceLight
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary
import com.example.signalclone.ui.theme.TextTertiary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppSettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onSelectChannel: (String) -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentUser by SignalRepository.currentUser.collectAsState()
    val currentLanguage by LocalizationManager.currentLanguage.collectAsState()
    val userAbout by SignalRepository.userAbout.collectAsState()
    val allMessagesMap by SignalRepository.messages.collectAsState()
    val channels by SignalRepository.channels.collectAsState()

    val appLockEnabled by SignalRepository.appLockEnabled.collectAsState()
    val readReceipts by SignalRepository.readReceipts.collectAsState()
    val chatWallpaper by SignalRepository.chatWallpaper.collectAsState()
    val chatTheme by SignalRepository.chatTheme.collectAsState()

    // Dialog & sheet states
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showQrDialog by remember { mutableStateOf(false) }
    var showStarredSheet by remember { mutableStateOf(false) }
    var showPrivacySheet by remember { mutableStateOf(false) }
    var showChatsSheet by remember { mutableStateOf(false) }
    var showStorageSheet by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showEditAboutDialog by remember { mutableStateOf(false) }
    var showAccountSheet by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }

    // Count starred messages across channels
    val starredMessagesCount = remember(allMessagesMap) {
        allMessagesMap.values.flatten().count { it.isStarred }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LocalizationManager.getString("settings"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("settings_back_btn")
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
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    LocalizationManager.getString("search_hint")
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF7F8FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ==========================================
            // 1. WhatsApp-Style Top Profile Card
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clickable { onNavigateToEditProfile() }
                    .testTag("whatsapp_profile_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(64.dp)) {
                        AvatarView(
                            avatarUrl = currentUser?.avatarUrl,
                            name = currentUser?.fullName ?: "Adam Dev",
                            size = 64.dp
                        )
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.BottomEnd)
                                .background(OnlineGreen, CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentUser?.fullName ?: "Adam Dev",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = SignalBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = userAbout,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "@${currentUser?.formattedUsername ?: "adam_dev_01"}",
                            fontSize = 12.sp,
                            color = SignalBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(
                        onClick = { showQrDialog = true },
                        modifier = Modifier
                            .size(44.dp)
                            .background(SignalBlue.copy(alpha = 0.08f), CircleShape)
                            .testTag("whatsapp_qr_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QR Code",
                            tint = SignalBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ==========================================
            // 2. WhatsApp Settings Categories
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    // Account
                    WhatsAppSettingItem(
                        icon = Icons.Default.Key,
                        iconBg = Color(0xFF10B981),
                        title = LocalizationManager.getString("account"),
                        subtitle = LocalizationManager.getString("account_subtitle"),
                        onClick = { showAccountSheet = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // Privacy
                    WhatsAppSettingItem(
                        icon = Icons.Default.Lock,
                        iconBg = Color(0xFF06B6D4),
                        title = LocalizationManager.getString("privacy"),
                        subtitle = LocalizationManager.getString("privacy_subtitle"),
                        onClick = { showPrivacySheet = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // Chats
                    WhatsAppSettingItem(
                        icon = Icons.Default.Chat,
                        iconBg = Color(0xFF22C55E),
                        title = LocalizationManager.getString("chats_settings"),
                        subtitle = LocalizationManager.getString("chats_settings_subtitle"),
                        onClick = { showChatsSheet = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // Starred Messages (الرسائل المميزة بنجمة)
                    WhatsAppSettingItem(
                        icon = Icons.Default.Star,
                        iconBg = Color(0xFFF59E0B),
                        title = LocalizationManager.getString("starred_messages"),
                        subtitle = if (starredMessagesCount > 0) "$starredMessagesCount ${LocalizationManager.getString("starred_messages")}" else LocalizationManager.getString("no_starred_messages"),
                        badgeText = if (starredMessagesCount > 0) "$starredMessagesCount" else null,
                        onClick = { showStarredSheet = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // Notifications
                    WhatsAppSettingItem(
                        icon = Icons.Default.Notifications,
                        iconBg = Color(0xFFF97316),
                        title = LocalizationManager.getString("notifications"),
                        subtitle = LocalizationManager.getString("notifications_subtitle"),
                        onClick = { showNotificationsSheet = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // Storage and Data
                    WhatsAppSettingItem(
                        icon = Icons.Default.Storage,
                        iconBg = Color(0xFF3B82F6),
                        title = LocalizationManager.getString("storage_and_data"),
                        subtitle = "${SignalRepository.storageUsed.collectAsState().value} used",
                        onClick = { showStorageSheet = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // App Language (لغة التطبيق)
                    WhatsAppSettingItem(
                        icon = Icons.Default.Language,
                        iconBg = Color(0xFF6366F1),
                        title = LocalizationManager.getString("app_language"),
                        subtitle = "${currentLanguage.flagEmoji} ${currentLanguage.nativeName} (${currentLanguage.englishName})",
                        onClick = { showLanguageSheet = true },
                        highlight = true
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // Help
                    WhatsAppSettingItem(
                        icon = Icons.Default.HelpOutline,
                        iconBg = Color(0xFF8B5CF6),
                        title = LocalizationManager.getString("help"),
                        subtitle = LocalizationManager.getString("help_subtitle"),
                        onClick = { showHelpDialog = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFF3F4F6))

                    // Invite a friend
                    WhatsAppSettingItem(
                        icon = Icons.Default.PersonAdd,
                        iconBg = Color(0xFFEC4899),
                        title = LocalizationManager.getString("invite_friend"),
                        subtitle = LocalizationManager.getString("app_subtitle"),
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Connect with me securely on Adam Signal: https://adamdev.io/signal/@adam_dev_01")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Invite via"))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 3. Sign Out Button
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .clickable { onSignOut() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(HangupRed.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sign Out",
                            tint = HangupRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = LocalizationManager.getString("sign_out"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HangupRed,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 4. WhatsApp / Meta Footer ("from Adam Dev")
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LocalizationManager.getString("meta_footer"),
                    fontSize = 12.sp,
                    color = TextTertiary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = SignalBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Adam Signal v2.6.0 (Quantum-Safe)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "End-to-End Encrypted Messaging Protocol",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }

    // ==========================================
    // DIALOGS & BOTTOM SHEETS
    // ==========================================

    // 1. Language Selector Sheet (Multilingual System)
    if (showLanguageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = SignalBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = LocalizationManager.getString("app_language"),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "اختر اللغة المناسبة لك - يتم تطبيقها فورياً على كامل واجهة التطبيق:",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppLanguage.values().forEach { lang ->
                    val isSelected = currentLanguage == lang
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                LocalizationManager.setLanguage(lang)
                                showLanguageSheet = false
                                scope.launch {
                                    snackbarHostState.showSnackbar("Language switched to ${lang.nativeName}")
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) SignalBlue.copy(alpha = 0.08f) else Color(0xFFF9FAFB)
                        ),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(SignalBlue, SignalBlue))) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = lang.flagEmoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 16.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SignalBlue else TextPrimary
                                )
                                Text(
                                    text = lang.englishName,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = SignalBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // 2. WhatsApp-Style QR Code Dialog
    if (showQrDialog) {
        var selectedQrTab by remember { mutableIntStateOf(0) }

        Dialog(onDismissRequest = { showQrDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("qr_code"),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        IconButton(onClick = { showQrDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    TabRow(
                        selectedTabIndex = selectedQrTab,
                        containerColor = Color(0xFFF3F4F6),
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedQrTab == 0,
                            onClick = { selectedQrTab = 0 },
                            text = { Text(LocalizationManager.getString("my_code"), fontWeight = FontWeight.SemiBold) }
                        )
                        Tab(
                            selected = selectedQrTab == 1,
                            onClick = { selectedQrTab = 1 },
                            text = { Text(LocalizationManager.getString("scan_code"), fontWeight = FontWeight.SemiBold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (selectedQrTab == 0) {
                        // My Code Card
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            border = CardDefaults.outlinedCardBorder(),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AvatarView(
                                    avatarUrl = currentUser?.avatarUrl,
                                    name = currentUser?.fullName ?: "Adam Dev",
                                    size = 56.dp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentUser?.fullName ?: "Adam Dev",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Adam Signal Security Key",
                                    fontSize = 12.sp,
                                    color = SignalBlue
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // QR Graphic Mock
                                Box(
                                    modifier = Modifier
                                        .size(160.dp)
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = "QR Code",
                                        tint = TextPrimary,
                                        modifier = Modifier.size(140.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = LocalizationManager.getString("qr_description"),
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("QR Code reset successfully.")
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(LocalizationManager.getString("reset_code"), fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "Scan my Adam Signal QR code to chat securely with me: https://adamdev.io/signal/@adam_dev_01")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share QR Code"))
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SignalBlue)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(LocalizationManager.getString("share_code"), fontSize = 13.sp)
                            }
                        }
                    } else {
                        // Scan Code Mock
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .background(Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Point camera at an Adam Signal QR code",
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Automatic instant encrypted pairing",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // 3. Starred Messages Sheet (الرسائل المميزة بنجمة)
    if (showStarredSheet) {
        val allStarred = remember(allMessagesMap, channels) {
            val list = mutableListOf<Pair<com.example.signalclone.data.model.Channel, Message>>()
            channels.forEach { ch ->
                val msgs = allMessagesMap[ch.id] ?: emptyList()
                msgs.filter { it.isStarred }.forEach { m ->
                    list.add(Pair(ch, m))
                }
            }
            list
        }

        ModalBottomSheet(
            onDismissRequest = { showStarredSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF59E0B).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = LocalizationManager.getString("starred_messages"),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${allStarred.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SignalBlue
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (allStarred.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = TextTertiary,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = LocalizationManager.getString("no_starred_messages"),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = LocalizationManager.getString("starred_hint"),
                                fontSize = 13.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(allStarred) { (channel, message) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showStarredSheet = false
                                        onSelectChannel(channel.id)
                                    },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = channel.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SignalBlue,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(
                                            onClick = {
                                                SignalRepository.toggleStarMessage(channel.id, message.id)
                                            },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Unstar",
                                                tint = Color(0xFFF59E0B),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = message.text.ifEmpty { "[Voice message or media]" },
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "By ${message.senderName}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 4. Privacy Sheet (الخصوصية)
    if (showPrivacySheet) {
        ModalBottomSheet(
            onDismissRequest = { showPrivacySheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = LocalizationManager.getString("privacy"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // App lock switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = SignalBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = LocalizationManager.getString("app_lock"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Unlock with fingerprint or face recognition",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = appLockEnabled,
                        onCheckedChange = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (it) "App lock enabled" else "App lock disabled"
                                )
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SignalBlue)
                    )
                }

                HorizontalDivider(color = Color(0xFFF3F4F6))

                // Read receipts
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = SignalBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = LocalizationManager.getString("read_receipts"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Show blue checkmarks when messages are read",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = readReceipts,
                        onCheckedChange = { },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SignalBlue)
                    )
                }

                HorizontalDivider(color = Color(0xFFF3F4F6))

                // End to end encryption badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(SignalBlue.copy(alpha = 0.07f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row {
                        Icon(Icons.Default.Security, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = LocalizationManager.getString("end_to_end_encrypted"),
                            fontSize = 12.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }

    // 5. Chats & Wallpaper Sheet (الدردشات والمظهر)
    if (showChatsSheet) {
        val wallpapers = listOf(
            Pair("Default", LocalizationManager.getString("wallpaper_default")),
            Pair("Teal", LocalizationManager.getString("wallpaper_teal")),
            Pair("Navy", LocalizationManager.getString("wallpaper_navy")),
            Pair("Amber", LocalizationManager.getString("wallpaper_amber")),
            Pair("Sky", LocalizationManager.getString("wallpaper_sky"))
        )

        ModalBottomSheet(
            onDismissRequest = { showChatsSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = LocalizationManager.getString("chats_settings"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = LocalizationManager.getString("chat_wallpaper"),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                wallpapers.forEach { (key, label) ->
                    val isSelected = chatWallpaper == key
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                SignalRepository.setChatWallpaper(key)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Wallpaper set to $label")
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { SignalRepository.setChatWallpaper(key) },
                            colors = RadioButtonDefaults.colors(selectedColor = SignalBlue)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = label, fontSize = 14.sp, color = if (isSelected) SignalBlue else TextPrimary, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }
    }

    // 6. Storage and Data Sheet (التخزين والبيانات)
    if (showStorageSheet) {
        ModalBottomSheet(
            onDismissRequest = { showStorageSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = LocalizationManager.getString("storage_and_data"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "2.4 GB used", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "64 GB free", color = TextSecondary, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.18f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SignalBlue,
                            trackColor = Color(0xFFE2E8F0)
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Sent: 1.2 GB", fontSize = 13.sp, color = TextSecondary)
                            Text(text = "Received: 3.8 GB", fontSize = 13.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }

    // 7. Help & App Info Dialog
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            icon = {
                Icon(Icons.Default.Security, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(36.dp))
            },
            title = {
                Text(
                    text = "Adam Signal Security & Architecture",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Engineered by Adam Dev with quantum-safe end-to-end encryption protocols.",
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "• All keys stored strictly in device enclave\n• Zero unencrypted telemetry\n• Verified Double Ratchet key exchange\n• Multilingual native RTL support",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "© 2026 Adam Dev. All Rights Reserved.",
                        fontSize = 12.sp,
                        color = SignalBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text(LocalizationManager.getString("close"), fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 8. Account Sheet
    if (showAccountSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAccountSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = LocalizationManager.getString("account"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                AccountOptionRow(
                    icon = Icons.Default.Security,
                    title = LocalizationManager.getString("security_keys"),
                    subtitle = "Ed25519 & Kyber-1024 active"
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                AccountOptionRow(
                    icon = Icons.Default.Key,
                    title = LocalizationManager.getString("passkeys"),
                    subtitle = "Biometric Passkey registered for @adam_dev"
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                AccountOptionRow(
                    icon = Icons.Default.Edit,
                    title = "Change Phone Number",
                    subtitle = "+1 (555) 019-2834"
                )
            }
        }
    }

    // 9. Notifications Sheet
    if (showNotificationsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotificationsSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = LocalizationManager.getString("notifications"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                NotificationToggleRow(
                    title = "Conversation Tones",
                    subtitle = "Play sounds for incoming and outgoing messages",
                    initial = true
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                NotificationToggleRow(
                    title = "High Priority Notifications",
                    subtitle = "Show previews of notifications at the top of screen",
                    initial = true
                )
                HorizontalDivider(color = Color(0xFFF3F4F6))
                NotificationToggleRow(
                    title = "Reaction Notifications",
                    subtitle = "Show notifications for reactions to messages you send",
                    initial = true
                )
            }
        }
    }
}

@Composable
fun WhatsAppSettingItem(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    badgeText: String? = null,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(iconBg.copy(alpha = 0.14f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconBg,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (highlight) SignalBlue else TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (badgeText != null) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFF59E0B), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFD1D5DB),
            modifier = Modifier.size(13.dp)
        )
    }
}

@Composable
fun AccountOptionRow(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
        }
    }
}

@Composable
fun NotificationToggleRow(title: String, subtitle: String, initial: Boolean) {
    var checked by remember { mutableStateOf(initial) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SignalBlue)
        )
    }
}
