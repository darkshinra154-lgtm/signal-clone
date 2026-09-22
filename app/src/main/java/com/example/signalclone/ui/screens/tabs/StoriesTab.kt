package com.example.signalclone.ui.screens.tabs

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.signalclone.localization.LocalizationManager
import com.example.signalclone.data.model.Story
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.components.AppMenuButton
import com.example.signalclone.ui.components.AvatarView
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary

@Composable
fun StoriesTab(
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onSignOut: () -> Unit
) {
    val currentUser by SignalRepository.currentUser.collectAsState()
    val stories by SignalRepository.stories.collectAsState()

    var showAddStoryDialog by remember { mutableStateOf(false) }
    var activeStoryToView by remember { mutableStateOf<Story?>(null) }

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
                    onNavigateToSettings = onNavigateToSettings,
                    onSignOut = onSignOut
                )

                Text(
                    text = LocalizationManager.getString("stories"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                IconButton(
                    onClick = { showAddStoryDialog = true },
                    modifier = Modifier.testTag("camera_story_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "New Story",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddStoryDialog = true },
                containerColor = SignalBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_story")
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Add Story",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // My Stories Item
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAddStoryDialog = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("my_stories_item"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(52.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        AvatarView(
                            name = currentUser?.fullName ?: "Me",
                            avatarUrl = currentUser?.avatarUrl,
                            size = 52.dp
                        )
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(SignalBlue)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Story",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "My Stories",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tap to add",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(start = 76.dp),
                    color = BorderLight
                )
            }

            // Recent Updates Section
            item {
                Text(
                    text = "Recent updates",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 18.dp, bottom = 8.dp)
                )
            }

            items(stories, key = { it.id }) { story ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { activeStoryToView = story }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag("story_item_${story.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Story avatar with blue gradient border
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .border(2.5.dp, SignalBlue, CircleShape)
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AvatarView(
                            name = story.userName,
                            avatarUrl = story.userAvatar,
                            size = 44.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = story.userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = story.textContent.ifEmpty { "Photo story" },
                            fontSize = 13.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }

                    Text(
                        text = story.timestamp,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(start = 76.dp),
                    color = BorderLight.copy(alpha = 0.5f)
                )
            }
        }
    }

    // Add Story Dialog
    if (showAddStoryDialog) {
        var storyText by remember { mutableStateOf("") }
        val sampleImages = listOf(
            "https://images.unsplash.com/photo-1518770660439-4636190af475?w=600",
            "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=600",
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600"
        )
        var selectedImageIndex by remember { mutableStateOf(0) }

        AlertDialog(
            onDismissRequest = { showAddStoryDialog = false },
            title = {
                Text(
                    text = "Add to My Story",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Share an encrypted photo or update with your contacts:",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )

                    OutlinedTextField(
                        value = storyText,
                        onValueChange = { storyText = it },
                        placeholder = { Text("What's on your mind?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("story_input_text")
                    )

                    // Preview selected photo
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedImageIndex = (selectedImageIndex + 1) % sampleImages.size
                            }
                    ) {
                        AsyncImage(
                            model = sampleImages[selectedImageIndex],
                            contentDescription = "Story preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Tap to change photo", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val text = storyText.ifBlank { "Encrypted story update 🔒" }
                        SignalRepository.addStory(text, sampleImages[selectedImageIndex])
                        showAddStoryDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
                    modifier = Modifier.testTag("publish_story_button")
                ) {
                    Text("Share Story", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddStoryDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // Story Fullscreen Viewer Dialog
    if (activeStoryToView != null) {
        val story = activeStoryToView!!
        Dialog(
            onDismissRequest = { activeStoryToView = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                if (story.mediaUrl != null) {
                    AsyncImage(
                        model = story.mediaUrl,
                        contentDescription = "Story view",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Top gradient overlay and header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AvatarView(
                                name = story.userName,
                                avatarUrl = story.userAvatar,
                                size = 36.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = story.userName,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = story.timestamp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        IconButton(onClick = { activeStoryToView = null }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Bottom text caption
                if (story.textContent.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(20.dp)
                    ) {
                        Text(
                            text = story.textContent,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
