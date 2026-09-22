package com.example.signalclone.ui.screens

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.HangupRed
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.SignalBlueLight
import com.example.signalclone.ui.theme.SurfaceLight
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit
) {
    val currentUser by SignalRepository.currentUser.collectAsState()
    val appLockEnabled by SignalRepository.appLockEnabled.collectAsState()
    val incognitoKeyboard by SignalRepository.incognitoKeyboard.collectAsState()
    val readReceipts by SignalRepository.readReceipts.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var firstName by remember(currentUser) { mutableStateOf(currentUser?.firstName ?: "Adam") }
    var lastName by remember(currentUser) { mutableStateOf(currentUser?.lastName ?: "Dev") }
    var username by remember(currentUser) { mutableStateOf(currentUser?.username ?: "adam_dev") }
    var usernameNumber by remember(currentUser) { mutableStateOf(currentUser?.usernameNumber ?: "01") }

    val presetAvatars = listOf(
        "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
        "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
        null
    )
    var selectedAvatarIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile & Settings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("profile_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Avatar with edit camera badge
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                AvatarView(
                    name = "$firstName $lastName".trim().ifEmpty { "Adam Dev" },
                    avatarUrl = currentUser?.avatarUrl,
                    size = 96.dp,
                    fontSize = 32,
                    isVerified = true
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SignalBlue)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable {
                            selectedAvatarIndex = (selectedAvatarIndex + 1) % presetAvatars.size
                            SignalRepository.updateProfile(
                                firstName = firstName,
                                lastName = lastName,
                                username = username,
                                number = usernameNumber,
                                avatarUrl = presetAvatars[selectedAvatarIndex]
                            )
                            scope.launch {
                                snackbarHostState.showSnackbar("Avatar updated!")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Change photo",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$firstName $lastName",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified Developer",
                    tint = SignalBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = "@${username}_$usernameNumber • ${currentUser?.role ?: "Creator"}",
                fontSize = 13.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Adam Dev Signature Card
            Card(
                colors = CardDefaults.cardColors(containerColor = SignalBlueLight),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SignalBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Adam Dev",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Adam Signal Messenger",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Developed & Designed by Adam Dev",
                            fontSize = 12.sp,
                            color = SignalBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Privacy & Security Controls
            Text(
                text = "Privacy & Security",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // App Lock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = SignalBlue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Biometric App Lock", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Require fingerprint to unlock app", fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                        Switch(
                            checked = appLockEnabled,
                            onCheckedChange = {
                                SignalRepository.toggleAppLock(it)
                                scope.launch {
                                    snackbarHostState.showSnackbar(if (it) "App Lock enabled! 🔒" else "App Lock disabled")
                                }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SignalBlue)
                        )
                    }

                    HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 10.dp))

                    // Incognito Keyboard
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Keyboard, contentDescription = null, tint = SignalBlue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Incognito Keyboard", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Disable personalized keyboard learning", fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                        Switch(
                            checked = incognitoKeyboard,
                            onCheckedChange = { SignalRepository.toggleIncognitoKeyboard(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SignalBlue)
                        )
                    }

                    HorizontalDivider(color = BorderLight, modifier = Modifier.padding(vertical = 10.dp))

                    // Read Receipts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MarkChatRead, contentDescription = null, tint = SignalBlue)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Read Receipts", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("Show double checkmarks when read", fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                        Switch(
                            checked = readReceipts,
                            onCheckedChange = { SignalRepository.toggleReadReceipts(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SignalBlue)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Edit Form
            Text(
                text = "Account Details",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SignalBlue,
                    unfocusedBorderColor = BorderLight,
                    focusedContainerColor = SurfaceLight,
                    unfocusedContainerColor = SurfaceLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_first_name")
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SignalBlue,
                    unfocusedBorderColor = BorderLight,
                    focusedContainerColor = SurfaceLight,
                    unfocusedContainerColor = SurfaceLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_last_name")
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SignalBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("profile_username")
                )

                OutlinedTextField(
                    value = usernameNumber,
                    onValueChange = { if (it.length <= 2) usernameNumber = it },
                    label = { Text("#") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SignalBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .width(76.dp)
                        .testTag("profile_username_number")
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    SignalRepository.updateProfile(
                        firstName = firstName,
                        lastName = lastName,
                        username = username,
                        number = usernameNumber,
                        avatarUrl = currentUser?.avatarUrl
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar("Profile saved successfully!")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("profile_save_button")
            ) {
                Text(
                    text = "Save Changes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    SignalRepository.signOut()
                    onSignOut()
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HangupRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("profile_signout_button")
            ) {
                Text(
                    text = "Sign Out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HangupRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Copyright Footer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Adam Signal v2.5.0 Pro",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
                Text(
                    text = "© 2026 Adam Dev. All Rights Reserved.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}
