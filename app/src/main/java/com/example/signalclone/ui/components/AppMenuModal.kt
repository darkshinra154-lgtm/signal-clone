package com.example.signalclone.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.data.model.User
import com.example.signalclone.localization.LocalizationManager
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.HangupRed
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary

@Composable
fun AppMenuButton(
    user: User?,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { expanded = true }
        ) {
            AvatarView(
                name = user?.fullName ?: "User",
                avatarUrl = user?.avatarUrl,
                size = 34.dp
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 8.dp),
            modifier = Modifier.width(240.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column {
                    Text(
                        text = user?.fullName ?: "Adam Dev",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Adam Signal Quantum",
                        fontSize = 12.sp,
                        color = SignalBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            HorizontalDivider(color = BorderLight)

            // Settings (WhatsApp Style)
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("settings"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = SignalBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onNavigateToSettings()
                }
            )

            // Profile Edit
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("profile"),
                            fontSize = 14.sp,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onNavigateToProfile()
                }
            )

            HorizontalDivider(color = BorderLight)

            // Sign Out
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("sign_out"),
                            fontSize = 14.sp,
                            color = HangupRed,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sign Out",
                            tint = HangupRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onSignOut()
                }
            )
        }
    }
}

@Composable
fun WhatsAppOverflowMenu(
    onNavigateToNewGroup: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onOpenStarredMessages: () -> Unit,
    onOpenLanguagePicker: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = TextPrimary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 0.dp, y = 4.dp),
            modifier = Modifier.width(230.dp)
        ) {
            // New group
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("new_group"),
                            fontSize = 14.sp,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.GroupAdd, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                },
                onClick = {
                    expanded = false
                    onNavigateToNewGroup()
                }
            )

            // Starred messages
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("starred_messages"),
                            fontSize = 14.sp,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
                    }
                },
                onClick = {
                    expanded = false
                    onOpenStarredMessages()
                }
            )

            // App language
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("app_language"),
                            fontSize = 14.sp,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.Language, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(18.dp))
                    }
                },
                onClick = {
                    expanded = false
                    onOpenLanguagePicker()
                }
            )

            HorizontalDivider(color = BorderLight)

            // Settings (WhatsApp Style)
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("settings"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.Settings, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(18.dp))
                    }
                },
                onClick = {
                    expanded = false
                    onNavigateToSettings()
                }
            )

            HorizontalDivider(color = BorderLight)

            // Sign out
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.getString("sign_out"),
                            fontSize = 14.sp,
                            color = HangupRed,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = HangupRed, modifier = Modifier.size(18.dp))
                    }
                },
                onClick = {
                    expanded = false
                    onSignOut()
                }
            )
        }
    }
}
