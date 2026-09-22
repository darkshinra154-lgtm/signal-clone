package com.example.signalclone.data.repository

import com.example.signalclone.data.model.CallRecord
import com.example.signalclone.data.model.Channel
import com.example.signalclone.data.model.Message
import com.example.signalclone.data.model.MessageStatus
import com.example.signalclone.data.model.Story
import com.example.signalclone.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object SignalRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.Default)

    val adamDevUser = User(
        id = "user_adam_dev",
        firstName = "Adam",
        lastName = "Dev",
        username = "adam_dev",
        usernameNumber = "01",
        email = "adam@adamdev.io",
        avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
        isCurrentUser = true,
        role = "Creator & Security Architect",
        isVerified = true
    )

    private val sampleContacts = listOf(
        User(
            id = "user_sarah",
            firstName = "Sarah",
            lastName = "Connor",
            username = "sarahc",
            usernameNumber = "99",
            email = "sarah@cyberdyne.org",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
            isVerified = true
        ),
        User(
            id = "user_elena",
            firstName = "Elena",
            lastName = "Rostova",
            username = "elena",
            usernameNumber = "14",
            email = "elena@signal.mock",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            isVerified = true
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

    private val _currentUser = MutableStateFlow<User?>(adamDevUser)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isSignedIn = MutableStateFlow(true)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    // Privacy & Security settings
    private val _appLockEnabled = MutableStateFlow(false)
    val appLockEnabled: StateFlow<Boolean> = _appLockEnabled.asStateFlow()

    private val _incognitoKeyboard = MutableStateFlow(true)
    val incognitoKeyboard: StateFlow<Boolean> = _incognitoKeyboard.asStateFlow()

    private val _readReceipts = MutableStateFlow(true)
    val readReceipts: StateFlow<Boolean> = _readReceipts.asStateFlow()

    // WhatsApp-style Settings
    private val _userAbout = MutableStateFlow("Hey there! I am using Adam Signal 🛡️")
    val userAbout: StateFlow<String> = _userAbout.asStateFlow()

    private val _lastSeenPrivacy = MutableStateFlow("Everyone")
    val lastSeenPrivacy: StateFlow<String> = _lastSeenPrivacy.asStateFlow()

    private val _profilePhotoPrivacy = MutableStateFlow("Everyone")
    val profilePhotoPrivacy: StateFlow<String> = _profilePhotoPrivacy.asStateFlow()

    private val _aboutPrivacy = MutableStateFlow("Everyone")
    val aboutPrivacy: StateFlow<String> = _aboutPrivacy.asStateFlow()

    private val _chatTheme = MutableStateFlow("System default")
    val chatTheme: StateFlow<String> = _chatTheme.asStateFlow()

    private val _chatWallpaper = MutableStateFlow("Default")
    val chatWallpaper: StateFlow<String> = _chatWallpaper.asStateFlow()

    private val _networkUsageSent = MutableStateFlow("1.2 GB")
    val networkUsageSent: StateFlow<String> = _networkUsageSent.asStateFlow()

    private val _networkUsageReceived = MutableStateFlow("3.8 GB")
    val networkUsageReceived: StateFlow<String> = _networkUsageReceived.asStateFlow()

    private val _storageUsed = MutableStateFlow("2.4 GB")
    val storageUsed: StateFlow<String> = _storageUsed.asStateFlow()

    private val _contacts = MutableStateFlow(sampleContacts)
    val contacts: StateFlow<List<User>> = _contacts.asStateFlow()

    private val initialChannels = listOf(
        Channel(
            id = "channel_note_to_self",
            name = "Note to Self 📝",
            isGroup = false,
            memberIds = listOf("user_adam_dev"),
            memberNames = listOf("Adam Dev"),
            lastMessage = "Encryption keys backed up securely with Adam Dev Vault.",
            lastMessageTime = "11:20 AM",
            unreadCount = 0,
            avatarUrl = null,
            isOnline = true,
            isPinned = true,
            isSelfNote = true
        ),
        Channel(
            id = "channel_core_team",
            name = "⚡ Adam Dev Core Team",
            isGroup = true,
            memberIds = listOf("user_adam_dev", "user_sarah", "user_marcus", "user_alex"),
            memberNames = listOf("Sarah", "Marcus", "Alex"),
            lastMessage = "Sarah: Adam Dev v2.5 update is ready for release!",
            lastMessageTime = "10:55 AM",
            unreadCount = 3,
            avatarUrl = null,
            isOnline = true,
            isPinned = true
        ),
        Channel(
            id = "channel_sarah",
            name = "Sarah Connor",
            isGroup = false,
            memberIds = listOf("user_adam_dev", "user_sarah"),
            memberNames = listOf("Sarah Connor"),
            lastMessage = "Let's review the Safety Number verification.",
            lastMessageTime = "10:42 AM",
            unreadCount = 1,
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
            isOnline = true,
            isPinned = true,
            ephemeralTimerSec = 60
        ),
        Channel(
            id = "channel_elena",
            name = "Elena Rostova",
            isGroup = false,
            memberIds = listOf("user_adam_dev", "user_elena"),
            memberNames = listOf("Elena Rostova"),
            lastMessage = "The HD video call quality in Adam Signal is crystal clear!",
            lastMessageTime = "Yesterday",
            unreadCount = 0,
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            isOnline = true
        ),
        Channel(
            id = "channel_marcus",
            name = "Marcus Vance",
            isGroup = false,
            memberIds = listOf("user_adam_dev", "user_marcus"),
            memberNames = listOf("Marcus Vance"),
            lastMessage = "Quantum-resistant ratchet applied to protocol.",
            lastMessageTime = "Sep 20",
            unreadCount = 0,
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            isOnline = false
        )
    )

    private val _channels = MutableStateFlow(initialChannels)
    val channels: StateFlow<List<Channel>> = _channels.asStateFlow()

    private val initialMessages = mutableMapOf<String, List<Message>>(
        "channel_note_to_self" to listOf(
            Message(
                id = "nts1",
                channelId = "channel_note_to_self",
                senderId = "user_adam_dev",
                senderName = "Adam Dev",
                text = "Welcome to your personal private vault! Everything written here is encrypted exclusively with your device key.",
                isSentByMe = true,
                timestamp = System.currentTimeMillis() - 7200000,
                status = MessageStatus.READ
            ),
            Message(
                id = "nts2",
                channelId = "channel_note_to_self",
                senderId = "user_adam_dev",
                senderName = "Adam Dev",
                text = "Encryption keys backed up securely with Adam Dev Vault.",
                isSentByMe = true,
                timestamp = System.currentTimeMillis() - 1800000,
                status = MessageStatus.READ,
                reactions = listOf("🔒", "⭐"),
                isStarred = true
            )
        ),
        "channel_core_team" to listOf(
            Message(
                id = "ct1",
                channelId = "channel_core_team",
                senderId = "user_alex",
                senderName = "Alex Chen",
                text = "All systems green for Adam Signal Pro Edition! 🚀",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 3600000 * 3,
                isStarred = true
            ),
            Message(
                id = "ct2",
                channelId = "channel_core_team",
                senderId = "user_marcus",
                senderName = "Marcus Vance",
                text = "The new UI themes and waveform audio notes look absolutely phenomenal.",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 3600000,
                reactions = listOf("🔥", "👏")
            ),
            Message(
                id = "ct3",
                channelId = "channel_core_team",
                senderId = "user_sarah",
                senderName = "Sarah Connor",
                text = "Sarah: Adam Dev v2.5 update is ready for release!",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 600000,
                status = MessageStatus.DELIVERED
            )
        ),
        "channel_sarah" to listOf(
            Message(
                id = "s1",
                channelId = "channel_sarah",
                senderId = "user_sarah",
                senderName = "Sarah Connor",
                text = "Hey Adam! Did you check out the new disappearing messages timer?",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 3600000 * 2,
                status = MessageStatus.READ
            ),
            Message(
                id = "s2",
                channelId = "channel_sarah",
                senderId = "user_adam_dev",
                senderName = "Adam Dev",
                text = "Yes, configured to 1 minute. It automatically wipes after reading ⏱️🔒",
                isSentByMe = true,
                timestamp = System.currentTimeMillis() - 3600000,
                status = MessageStatus.READ,
                reactions = listOf("❤️", "🔥")
            ),
            Message(
                id = "s3",
                channelId = "channel_sarah",
                senderId = "user_sarah",
                senderName = "Sarah Connor",
                text = "Let's review the Safety Number verification.",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 900000,
                status = MessageStatus.DELIVERED
            )
        ),
        "channel_elena" to listOf(
            Message(
                id = "e1",
                channelId = "channel_elena",
                senderId = "user_elena",
                senderName = "Elena Rostova",
                text = "The HD video call quality in Adam Signal is crystal clear!",
                isSentByMe = false,
                timestamp = System.currentTimeMillis() - 86400000,
                reactions = listOf("🌟")
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
            timestamp = "Sep 20, 8:20 PM",
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
            textContent = "Securing networks in the mountains with Adam Signal 🏔️⚡",
            timestamp = "2h ago",
            viewsCount = 78
        ),
        Story(
            id = "s2",
            userId = "user_marcus",
            userName = "Marcus Vance",
            userAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            mediaUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=600",
            textContent = "Adam Dev v2.5 Architecture deployed! 💻🔒",
            timestamp = "4h ago",
            viewsCount = 120
        ),
        Story(
            id = "s3",
            userId = "user_maya",
            userName = "Maya Lin",
            userAvatar = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150",
            mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600",
            textContent = "Sunset by the ocean 🌅",
            timestamp = "7h ago",
            viewsCount = 54
        )
    )

    private val _stories = MutableStateFlow(initialStories)
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    fun signIn(email: String, pass: String): Boolean {
        if (email.isNotBlank()) {
            val name = email.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
            _currentUser.value = User(
                id = "user_adam_dev",
                firstName = name,
                lastName = "Dev",
                username = email.substringBefore("@"),
                usernameNumber = "01",
                email = email,
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
                isCurrentUser = true,
                role = "Lead Developer",
                isVerified = true
            )
            _isSignedIn.value = true
            return true
        }
        return false
    }

    fun signUp(firstName: String, lastName: String, username: String, number: String, email: String): Boolean {
        _currentUser.value = User(
            id = "user_adam_dev",
            firstName = firstName,
            lastName = lastName,
            username = username,
            usernameNumber = number.ifBlank { "01" },
            email = email,
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            isCurrentUser = true,
            role = "Creator",
            isVerified = true
        )
        _isSignedIn.value = true
        return true
    }

    fun signOut() {
        _isSignedIn.value = false
    }

    fun toggleAppLock(enabled: Boolean) {
        _appLockEnabled.value = enabled
    }

    fun toggleIncognitoKeyboard(enabled: Boolean) {
        _incognitoKeyboard.value = enabled
    }

    fun toggleReadReceipts(enabled: Boolean) {
        _readReceipts.value = enabled
    }

    fun togglePinChannel(channelId: String) {
        _channels.value = _channels.value.map {
            if (it.id == channelId) it.copy(isPinned = !it.isPinned) else it
        }.sortedWith(compareByDescending<Channel> { it.isPinned })
    }

    fun setEphemeralTimer(channelId: String, timerSec: Int) {
        _channels.value = _channels.value.map {
            if (it.id == channelId) it.copy(ephemeralTimerSec = timerSec) else it
        }
    }

    fun updateProfile(firstName: String, lastName: String, username: String, number: String, avatarUrl: String?) {
        val current = _currentUser.value ?: adamDevUser
        _currentUser.value = current.copy(
            firstName = firstName,
            lastName = lastName,
            username = username,
            usernameNumber = number,
            avatarUrl = avatarUrl ?: current.avatarUrl
        )
    }

    fun sendMessage(
        channelId: String,
        text: String,
        imageUrl: String? = null,
        isAudio: Boolean = false,
        audioSec: Int = 0,
        replyToSender: String? = null,
        replyToText: String? = null
    ) {
        val current = _currentUser.value ?: adamDevUser
        val channel = _channels.value.find { it.id == channelId }
        val timer = channel?.ephemeralTimerSec ?: 0

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
            status = MessageStatus.DELIVERED,
            expiresInSec = timer,
            replyToSender = replyToSender,
            replyToText = replyToText
        )

        val updatedMap = _messages.value.toMutableMap()
        val list = (updatedMap[channelId] ?: emptyList()).toMutableList()
        list.add(newMsg)
        updatedMap[channelId] = list
        _messages.value = updatedMap

        val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        _channels.value = _channels.value.map { ch ->
            if (ch.id == channelId) {
                ch.copy(
                    lastMessage = if (isAudio) "Voice note (${audioSec}s)" else (if (imageUrl != null) "📷 Photo" else text),
                    lastMessageTime = timeStr,
                    unreadCount = 0
                )
            } else ch
        }

        // Live simulation: simulate realistic contact response if chatting with Sarah or Elena
        if (channel != null && !channel.isSelfNote && !isAudio && text.isNotBlank()) {
            triggerRealisticContactReply(channelId, channel.name)
        }
    }

    private fun triggerRealisticContactReply(channelId: String, contactName: String) {
        repositoryScope.launch {
            delay(1200)
            // Mark channel as typing
            _channels.value = _channels.value.map {
                if (it.id == channelId) it.copy(isTyping = true, typingUser = contactName) else it
            }

            delay(2000)

            // Realistic reply based on Adam Signal
            val replies = listOf(
                "Got it! Adam Signal's encryption is working seamlessly 🛡️",
                "Sounds great! Thanks for keeping our conversations private.",
                "Awesome update! The UI by Adam Dev is top tier 🔥",
                "Received loud and clear! Talk soon 👍",
                "Checked and verified the Safety Number. Everything matches! 🔒"
            )
            val replyText = replies.random()

            val replyMsg = Message(
                id = UUID.randomUUID().toString(),
                channelId = channelId,
                senderId = "contact_$channelId",
                senderName = contactName,
                text = replyText,
                isSentByMe = false,
                timestamp = System.currentTimeMillis(),
                status = MessageStatus.DELIVERED
            )

            val updatedMap = _messages.value.toMutableMap()
            val list = (updatedMap[channelId] ?: emptyList()).toMutableList()
            list.add(replyMsg)
            updatedMap[channelId] = list
            _messages.value = updatedMap

            val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
            _channels.value = _channels.value.map {
                if (it.id == channelId) {
                    it.copy(
                        isTyping = false,
                        typingUser = null,
                        lastMessage = replyText,
                        lastMessageTime = timeStr
                    )
                } else it
            }
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
            memberIds = listOf(_currentUser.value?.id ?: "user_adam_dev", contact.id),
            memberNames = listOf(contact.fullName),
            lastMessage = "Adam Signal verified secure session established.",
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
            memberIds = listOf(_currentUser.value?.id ?: "user_adam_dev") + selectedUserIds,
            memberNames = memberNames,
            lastMessage = "Group created with Adam Dev Encryption Protocol",
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
        val current = _currentUser.value ?: adamDevUser
        val story = Story(
            id = UUID.randomUUID().toString(),
            userId = current.id,
            userName = current.fullName,
            userAvatar = current.avatarUrl,
            mediaUrl = mediaUrl,
            textContent = text,
            timestamp = "Just now",
            isViewed = false,
            viewsCount = 1
        )
        _stories.value = listOf(story) + _stories.value
    }

    fun toggleStarMessage(channelId: String, messageId: String) {
        val currentMap = _messages.value.toMutableMap()
        val list = (currentMap[channelId] ?: emptyList()).map { msg ->
            if (msg.id == messageId) msg.copy(isStarred = !msg.isStarred) else msg
        }
        currentMap[channelId] = list
        _messages.value = currentMap
    }

    fun deleteMessage(channelId: String, messageId: String) {
        val currentMap = _messages.value.toMutableMap()
        val list = (currentMap[channelId] ?: emptyList()).filterNot { it.id == messageId }
        currentMap[channelId] = list
        _messages.value = currentMap
    }

    fun clearChat(channelId: String) {
        val currentMap = _messages.value.toMutableMap()
        currentMap[channelId] = emptyList()
        _messages.value = currentMap
    }

    fun toggleMuteChannel(channelId: String) {
        _channels.value = _channels.value.map {
            if (it.id == channelId) it.copy(isMuted = !it.isMuted) else it
        }
    }

    fun setChannelWallpaper(channelId: String, wallpaper: String?) {
        _channels.value = _channels.value.map {
            if (it.id == channelId) it.copy(customWallpaper = wallpaper) else it
        }
    }

    fun updateAbout(bio: String) {
        _userAbout.value = bio
    }

    fun setChatTheme(theme: String) {
        _chatTheme.value = theme
    }

    fun setChatWallpaper(wallpaper: String) {
        _chatWallpaper.value = wallpaper
    }

    fun setLastSeenPrivacy(setting: String) {
        _lastSeenPrivacy.value = setting
    }

    fun setProfilePhotoPrivacy(setting: String) {
        _profilePhotoPrivacy.value = setting
    }

    fun setAboutPrivacy(setting: String) {
        _aboutPrivacy.value = setting
    }
}
