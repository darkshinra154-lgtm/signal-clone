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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.HangupRed
import com.example.signalclone.ui.theme.SignalBlue
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
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var firstName by remember(currentUser) { mutableStateOf(currentUser?.firstName ?: "") }
    var lastName by remember(currentUser) { mutableStateOf(currentUser?.lastName ?: "") }
    var username by remember(currentUser) { mutableStateOf(currentUser?.username ?: "") }
    var usernameNumber by remember(currentUser) { mutableStateOf(currentUser?.usernameNumber ?: "01") }

    val presetAvatars = listOf(
        "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
        "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
        null
    )
    var selectedAvatarIndex by remember { mutableStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar with edit photo button
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                AvatarView(
                    name = "$firstName $lastName".trim().ifEmpty { "User" },
                    avatarUrl = currentUser?.avatarUrl,
                    size = 96.dp,
                    fontSize = 32
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

            Text(
                text = "${username}_$usernameNumber",
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Inputs
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

            Spacer(modifier = Modifier.height(14.dp))

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

            Spacer(modifier = Modifier.height(14.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

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
        }
    }
}
