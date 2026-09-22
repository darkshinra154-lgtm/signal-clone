package com.example.signalclone.data.repository

import com.example.signalclone.data.model.CallRecord
import com.example.signalclone.data.model.Channel
import com.example.signalclone.data.model.Message
import com.example.signalclone.data.model.MessageStatus
import com.example.signalclone.data.model.Story
import com.example.signalclone.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object SignalRepository {

    private val sampleContacts = listOf(
        User(
            id = "user_sarah",
            firstName = "Sarah",
            lastName = "Connor",
            username = "sarahc",
            usernameNumber = "99",
            email = "sarah@cyberdyne.org",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150"
        ),
        User(
            id = "user_elena",
            firstName = "Elena",
            lastName = "Rostova",
            username = "elena",
            usernameNumber = "14",
            email = "elena@signal.mock",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150"
        ),
        User(
            id = "user_marcus",
            firstName = "Marcus",
            lastName = "Vance",
            username = "mvance",
            usernameNumber = "07",
            email = "marcus@signal.mock",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150"
        ),
        User(
            id = "user_alex",
            firstName = "Alex",
            lastName = "Chen",
            username = "alexchen",
            usernameNumber = "42",
            email = "alex@signal.mock",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150"
        ),
        User(
            id = "user_maya",
            firstName = "Maya",
            lastName = "Lin",
            username = "mayal",
            usernameNumber = "23",
            email = "maya@signal.mock",
            avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150"
        )
    )

    private val defaultUser = User(
        id = "current_user",
        firstName = "John",
        lastName = "Doe",
        username = "johndoe",
        usernameNumber = "01",
        email = "johndoe@signal.mock",
        avatarUrl = null,
        isCurrentUser = true
    )

    private val _currentUser = MutableStateFlow<User?>(defaultUser)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isSignedIn = MutableStateFlow(true)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    private val _contacts = MutableStateFlow(sampleContacts)
    val contacts: StateFlow<List<User>> = _contacts.asStateFlow()

    private val initialChannels = listOf(
        Channel(
            id = "channel_sarah",
            name = "Sarah Connor",
            isGroup = false,
            memberIds = listOf("current_user", "user_sarah"),
            memberNames = listOf("Sarah Connor"),
            lastMessage = "Let's meet tomorrow to review the encryption keys.",
            lastMessageTime = "10:42 AM",
            unreadCount = 2,
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
            isOnline = true
        ),
        Channel(
            id = "channel_project_privacy",
            name = "🔐 Privacy Tech WG",
            isGroup = true,
            memberIds = listOf("current_user", "user_sarah", "user_marcus", "user_alex"),
            memberNames = listOf("Sarah", "Marcus", "Alex"),
            lastMessage = "Marcus: Zero-knowledge proofs deployed successfully!",
            lastMessageTime = "Yesterday",
            unreadCount = 0,
            avatarUrl = null
        ),
        Channel(
            id = "channel_elena",
            name = "Elena Rostova",
            isGroup = false,
            memberIds = listOf("current_user", "user_elena"),
            memberNames = listOf("Elena Rostova"),
            lastMessage = "Thanks for the secure voice call earlier 👍",
            lastMessageTime = "Sep 20",
            unreadCount = 0,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            isOnline = false
        ),
        Channel(
            id = "channel_marcus",
            name = "Marcus Vance",
            isGroup = false,
            memberIds = listOf("current_user", "user_marcus"),
            memberNames = listOf("Marcus Vance"),
            lastMessage = "Can you send the contract doc when you get a chance?",
            lastMessageTime = "Sep 18",
            unreadCount = 0,
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            isOnline = true
        )
    )

    private val _channels = MutableStateFlow(initialChannels)
    val channels: StateFlow<List<Channel>> = _channels.asStateFlow()

    private val initialMessages = mutableMapOf<String, List<Message>>(
        "channel_sarah" to listOf(
            Message(
                id = "m1",
                channelId = "channel_sarah",
                senderId = "user_sarah",
                senderName = "Sarah Connor",
                text = "Hey John! Did you verify the Safety Number for our chat?",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 3600000 * 2,
                status = MessageStatus.READ
            ),
            Message(
                id = "m2",
                channelId = "channel_sarah",
                senderId = "current_user",
                senderName = "John Doe",
                text = "Yes, scanned the QR code yesterday. Everything matches up perfectly 🔒",
                isSentByMe = true,
                timestamp = System.currentTimeMillis() - 3600000,
                status = MessageStatus.READ,
                reactions = listOf("❤️")
            ),
            Message(
                id = "m3",
                channelId = "channel_sarah",
                senderId = "user_sarah",
                senderName = "Sarah Connor",
                text = "Awesome! Let's meet tomorrow to review the encryption keys.",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 600000,
                status = MessageStatus.DELIVERED
            )
        ),
        "channel_project_privacy" to listOf(
            Message(
                id = "p1",
                channelId = "channel_project_privacy",
                senderId = "user_alex",
                senderName = "Alex Chen",
                text = "Welcome everyone to the end-to-end encrypted project channel.",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 86400000 * 2
            ),
            Message(
                id = "p2",
                channelId = "channel_project_privacy",
                senderId = "user_marcus",
                senderName = "Marcus Vance",
                text = "Zero-knowledge proofs deployed successfully!",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 86400000,
                reactions = listOf("🎉", "🚀")
            )
        ),
        "channel_elena" to listOf(
            Message(
                id = "e1",
                channelId = "channel_elena",
                senderId = "user_elena",
                senderName = "Elena Rostova",
                text = "Thanks for the secure voice call earlier 👍",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 86400000 * 2
            )
        )
    )

    private val _messages = MutableStateFlow<Map<String, List<Message>>>(initialMessages)
    val messages: StateFlow<Map<String, List<Message>>> = _messages.asStateFlow()

    private val initialCalls = listOf(
        CallRecord(
            id = "c1",
            contactId = "user_sarah",
            contactName = "Sarah Connor",
            contactAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
            isVideo = true,
            isIncoming = true,
            isMissed = false,
            timestamp = "Today, 10:45 AM",
            duration = "14m 20s"
        ),
        CallRecord(
            id = "c2",
            contactId = "user_marcus",
            contactName = "Marcus Vance",
            contactAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            isVideo = false,
            isIncoming = false,
            isMissed = false,
            timestamp = "Yesterday, 3:12 PM",
            duration = "4m 05s"
        ),
        CallRecord(
            id = "c3",
            contactId = "user_elena",
            contactName = "Elena Rostova",
            contactAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            isVideo = true,
            isIncoming = true,
            isMissed = true,
            timestamp = "Sep 19, 8:20 PM",
            duration = "Missed"
        )
    )

    private val _calls = MutableStateFlow(initialCalls)
    val calls: StateFlow<List<CallRecord>> = _calls.asStateFlow()

    private val initialStories = listOf(
        Story(
            id = "s1",
            userId = "user_sarah",
            userName = "Sarah Connor",
            userAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
            mediaUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=600",
            textContent = "Securing networks in the mountains 🏔️⚡",
            timestamp = "2h ago"
        ),
        Story(
            id = "s2",
            userId = "user_marcus",
            userName = "Marcus Vance",
            userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            mediaUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=600",
            textContent = "Building modern privacy-first tools 💻🔒",
            timestamp = "5h ago"
        ),
        Story(
            id = "s3",
            userId = "user_maya",
            userName = "Maya Lin",
            userAvatar = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150",
            mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600",
            textContent = "Sunset by the ocean 🌅",
            timestamp = "8h ago"
        )
    )

    private val _stories = MutableStateFlow(initialStories)
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    fun signIn(email: String, pass: String): Boolean {
        if (email.isNotBlank()) {
            val name = email.substringBefore("@").replace(".", " ").capitalize(Locale.ROOT)
            _currentUser.value = User(
                id = "current_user",
                firstName = name,
                lastName = "",
                username = email.substringBefore("@"),
                usernameNumber = "01",
                email = email,
                isCurrentUser = true
            )
            _isSignedIn.value = true
            return true
        }
        return false
    }

    fun signUp(firstName: String, lastName: String, username: String, number: String, email: String): Boolean {
        _currentUser.value = User(
            id = "current_user",
            firstName = firstName,
            lastName = lastName,
            username = username,
            usernameNumber = number.ifBlank { "01" },
            email = email,
            isCurrentUser = true
        )
        _isSignedIn.value = true
        return true
    }

    fun signOut() {
        _isSignedIn.value = false
    }

    fun updateProfile(firstName: String, lastName: String, username: String, number: String, avatarUrl: String?) {
        val current = _currentUser.value ?: defaultUser
        _currentUser.value = current.copy(
            firstName = firstName,
            lastName = lastName,
            username = username,
            usernameNumber = number,
            avatarUrl = avatarUrl ?: current.avatarUrl
        )
    }

    fun sendMessage(channelId: String, text: String, imageUrl: String? = null, isAudio: Boolean = false, audioSec: Int = 0) {
        val current = _currentUser.value ?: defaultUser
        val newMsg = Message(
            id = UUID.randomUUID().toString(),
            channelId = channelId,
            senderId = current.id,
            senderName = current.fullName,
            senderAvatar = current.avatarUrl,
            text = text,
            imageUrl = imageUrl,
            isAudio = isAudio,
            audioDurationSec = audioSec,
            isSentByMe = true,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.DELIVERED
        )

        val updatedMap = _messages.value.toMutableMap()
        val list = (updatedMap[channelId] ?: emptyList()).toMutableList()
        list.add(newMsg)
        updatedMap[channelId] = list
        _messages.value = updatedMap

        // Update channel last message
        val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        _channels.value = _channels.value.map { ch ->
            if (ch.id == channelId) {
                ch.copy(
                    lastMessage = if (isAudio) "Voice message (${audioSec}s)" else (if (imageUrl != null) "📷 Photo" else text),
                    lastMessageTime = timeStr,
                    unreadCount = 0
                )
            } else ch
        }
    }

    fun addReaction(channelId: String, messageId: String, emoji: String) {
        val updatedMap = _messages.value.toMutableMap()
        val list = (updatedMap[channelId] ?: emptyList()).map { msg ->
            if (msg.id == messageId) {
                val reactions = msg.reactions.toMutableList()
                if (reactions.contains(emoji)) {
                    reactions.remove(emoji)
                } else {
                    reactions.add(emoji)
                }
                msg.copy(reactions = reactions)
            } else msg
        }
        updatedMap[channelId] = list
        _messages.value = updatedMap
    }

    fun getOrCreateDirectChannel(contact: User): Channel {
        val existing = _channels.value.find { !it.isGroup && it.memberIds.contains(contact.id) }
        if (existing != null) return existing

        val newChannel = Channel(
            id = "channel_${contact.id}",
            name = contact.fullName,
            isGroup = false,
            memberIds = listOf(_currentUser.value?.id ?: "current_user", contact.id),
            memberNames = listOf(contact.fullName),
            lastMessage = "Signal connection verified.",
            lastMessageTime = "Just now",
            unreadCount = 0,
            avatarUrl = contact.avatarUrl,
            isOnline = true
        )
        _channels.value = listOf(newChannel) + _channels.value
        return newChannel
    }

    fun createGroup(groupName: String, selectedUserIds: List<String>): Channel {
        val memberContacts = _contacts.value.filter { selectedUserIds.contains(it.id) }
        val memberNames = memberContacts.map { it.firstName }
        val newChannel = Channel(
            id = "channel_group_${System.currentTimeMillis()}",
            name = groupName,
            isGroup = true,
            memberIds = listOf(_currentUser.value?.id ?: "current_user") + selectedUserIds,
            memberNames = memberNames,
            lastMessage = "Group created",
            lastMessageTime = "Just now",
            unreadCount = 0,
            avatarUrl = null
        )
        _channels.value = listOf(newChannel) + _channels.value
        return newChannel
    }

    fun searchUsers(query: String): List<User> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return _contacts.value
        return _contacts.value.filter {
            it.username.lowercase().contains(q) ||
            it.formattedUsername.lowercase().contains(q) ||
            it.fullName.lowercase().contains(q)
        }
    }

    fun addCallRecord(contactName: String, contactAvatar: String?, isVideo: Boolean, isIncoming: Boolean, isMissed: Boolean, duration: String) {
        val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val newCall = CallRecord(
            id = UUID.randomUUID().toString(),
            contactId = "call_${System.currentTimeMillis()}",
            contactName = contactName,
            contactAvatar = contactAvatar,
            isVideo = isVideo,
            isIncoming = isIncoming,
            isMissed = isMissed,
            timestamp = "Today, $timeStr",
            duration = duration
        )
        _calls.value = listOf(newCall) + _calls.value
    }

    fun addStory(text: String, mediaUrl: String?) {
        val current = _currentUser.value ?: defaultUser
        val story = Story(
            id = UUID.randomUUID().toString(),
            userId = current.id,
            userName = current.fullName,
            userAvatar = current.avatarUrl,
            mediaUrl = mediaUrl,
            textContent = text,
            timestamp = "Just now",
            isViewed = false
        )
        _stories.value = listOf(story) + _stories.value
    }
}
