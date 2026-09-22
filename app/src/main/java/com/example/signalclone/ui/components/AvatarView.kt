package com.example.signalclone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.signalclone.ui.theme.OnlineGreen
import com.example.signalclone.ui.theme.SignalBlue

private val avatarColors = listOf(
    Color(0xFF2563EB),
    Color(0xFF7C3AED),
    Color(0xFFDB2777),
    Color(0xFFEA580C),
    Color(0xFF059669),
    Color(0xFF0891B2),
    Color(0xFF4F46E5)
)

fun getAvatarColor(name: String): Color {
    if (name.isEmpty()) return avatarColors[0]
    val hash = kotlin.math.abs(name.hashCode())
    return avatarColors[hash % avatarColors.size]
}

@Composable
fun AvatarView(
    name: String,
    avatarUrl: String? = null,
    isGroup: Boolean = false,
    size: Dp = 40.dp,
    fontSize: Int = (size.value * 0.4).toInt(),
    isOnline: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else if (isGroup) {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(Color(0xFF6366F1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Group",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.55f)
                )
            }
        } else {
            val initials = name.split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .map { it.first().uppercaseChar() }
                .joinToString("")
                .ifEmpty { "?" }

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(getAvatarColor(name)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.3f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(OnlineGreen)
                    .border(1.5.dp, Color.White, CircleShape)
            )
        }
    }
}
