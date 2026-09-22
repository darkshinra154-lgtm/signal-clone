package com.example.signalclone.data.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val usernameNumber: String = "01",
    val avatarUrl: String? = null,
    val email: String = "",
    val isCurrentUser: Boolean = false,
    val role: String = "Member",
    val isVerified: Boolean = false
) {
    val fullName: String
        get() = "$firstName $lastName".trim()

    val formattedUsername: String
        get() = if (usernameNumber.isNotBlank()) "${username}_$usernameNumber" else username
}

data class Message(
    val id: String,
    val channelId: String,
    val senderId: String,
    val senderName: String,
    val senderAvatar: String? = null,
    val text: String = "",
    val imageUrl: String? = null,
    val isAudio: Boolean = false,
    val audioDurationSec: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isSentByMe: Boolean = false,
    val status: MessageStatus = MessageStatus.DELIVERED,
    val reactions: List<String> = emptyList(),
    val expiresInSec: Int = 0,
    val isStarred: Boolean = false,
    val replyToSender: String? = null,
    val replyToText: String? = null
)

enum class MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ
}

data class Channel(
    val id: String,
    val name: String,
    val isGroup: Boolean = false,
    val memberIds: List<String> = emptyList(),
    val memberNames: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: String = "",
    val unreadCount: Int = 0,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val isPinned: Boolean = false,
    val isSelfNote: Boolean = false,
    val ephemeralTimerSec: Int = 0,
    val isTyping: Boolean = false,
    val typingUser: String? = null,
    val customWallpaper: String? = null,
    val isMuted: Boolean = false
)

data class CallRecord(
    val id: String,
    val contactId: String,
    val contactName: String,
    val contactAvatar: String? = null,
    val isVideo: Boolean = false,
    val isIncoming: Boolean = true,
    val isMissed: Boolean = false,
    val timestamp: String = "Today, 10:45 AM",
    val duration: String = "3m 12s"
)

data class Story(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String? = null,
    val mediaUrl: String? = null,
    val textContent: String = "",
    val timestamp: String = "2h ago",
    val isViewed: Boolean = false,
    val viewsCount: Int = 42
)
