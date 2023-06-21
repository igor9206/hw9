class Service {
    private var uniqueUserId = 0
    private var uniqueChatsId = 0
    private var uniqueMsgId = 0
    private var chats = mutableListOf<Chats>()
    private var users = mutableListOf<Users>()

    fun addUser(name: String): Boolean {
        return users.add(Users(uniqueUserId++, name))
    }

    fun sendMessage(senderId: Int, addresseeId: Int, textMessage: String = String()): Boolean {
        checkUser(senderId)
        checkUser(addresseeId)

        chats.filter {
            it.user1.id == senderId && it.user2.id == addresseeId
                    || it.user1.id == addresseeId && it.user2.id == senderId
        }
            .ifEmpty {
                chats.add(
                    Chats(
                        uniqueChatsId++,
                        users[senderId],
                        users[addresseeId]
                    )
                )
            }
        if (textMessage.isNotEmpty()) {
            return chats.first {
                it.user1.id == senderId && it.user2.id == addresseeId
                        || it.user1.id == addresseeId && it.user2.id == senderId
            }
                .messages.add(Messages(uniqueMsgId++, textMessage, users[senderId]))
        }
        return false
    }

    fun getUnreadChatsCount(userId: Int): Int {
        checkUser(userId)
        return chats.filter { it.user1.id == userId || it.user2.id == userId }
            .count { it -> it.messages.any { !it.readIt && it.sender != users[userId] } }
    }

    fun getChats(userId: Int): List<Chats> {
        checkUser(userId)
        return chats.filter { it.user1.id == userId || it.user2.id == userId }
    }

    fun getLastMsgInChat(userId: Int): List<Messages> {
        checkUser(userId)
        return chats.filter { it.user1.id == userId || it.user2.id == userId }
            .map { it.messages.last() }
    }

    fun getListMsg(chatId: Int, lastMsgId: Int, amountMsg: Int): List<Messages> {
        val chat = checkChat(chatId)
        val firstMsg = chat.messages
            .firstOrNull { it.id == lastMsgId } ?: throw NotFoundException("No message with ID:$lastMsgId")
        return chat.messages
            .subList(chat.messages.indexOf(firstMsg), chat.messages.size)
            .take(amountMsg)
            .onEach { it.readIt = true }
    }

    fun delMessage(chatId: Int, messageId: Int): Boolean {
        val chat = checkChat(chatId)
        val msg = chat.messages
            .firstOrNull { it.id == messageId } ?: throw NotFoundException("No message with ID:$messageId")
        return chat.messages.remove(msg)
    }

    fun delChat(chatId: Int): Boolean {
        val chat = checkChat(chatId)
        return chats.remove(chat)
    }

    private fun checkUser(userId: Int): Users {
        return users.firstOrNull { it.id == userId } ?: throw NotFoundException("No user with id:${userId}")
    }

    private fun checkChat(chatId: Int): Chats {
        return chats.firstOrNull { it.id == chatId } ?: throw NotFoundException("No chats with ChatID:$chatId")
    }

}