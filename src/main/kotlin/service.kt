class Service {
    private val errorNotFound = -1
    private var uniqueUserId = 0
    private var uniqueChatsId = 0
    private var uniqueMsgId = 0
    private var chats = mutableListOf<Chats>()
    private var users = mutableListOf<Users>()

    fun addUser(name: String): Boolean {
        return users.add(Users(uniqueUserId++, name))
    }

    fun sendMessage(senderId: Int, addresseeId: Int, textMessage: String = String()): Boolean {
        if (checkUser(senderId) != errorNotFound && checkUser(addresseeId) != errorNotFound) {
            if (checkChat(senderId, addresseeId) == errorNotFound) {
                return chats.add(
                    Chats(
                        uniqueChatsId++,
                        users[checkUser(senderId)],
                        users[checkUser(addresseeId)],
                        if (textMessage.isNotEmpty()) {
                            mutableListOf(Messages(uniqueMsgId++, textMessage, users[checkUser(senderId)]))
                        } else {
                            mutableListOf()
                        }
                    )
                )
            } else {
                if (textMessage.isNotEmpty()) {
                    return chats[checkChat(senderId, addresseeId)].messages.add(
                        Messages(uniqueMsgId++, textMessage, users[checkUser(senderId)])
                    )
                }
            }

        } else {
            throw NotFoundException("Пользователя не существует")
        }
        return false
    }

    fun getUnreadChatsCount(userId: Int): Int {
        val user = checkUser(userId)
        if (user != errorNotFound) {
            val userChats = chats.filter { it.user1 == users[user] || it.user2 == users[user] }
            return userChats.count { it -> it.messages.any { !it.readIt && it.sender.name != users[user].name } }
        }
        return errorNotFound
    }

    fun getChats(userId: Int): String {
        val user = checkUser(userId)
        if (user != errorNotFound) {
            val filterChat = chats.filter { it.user1 == users[user] || it.user2 == users[user] }
            var listChats = String()
            for (i in filterChat) {
                listChats += "Chat: id${i.id}  ${i.user1}  ${i.user2} \n"
            }
            return listChats
        }
        throw NotFoundException("Пользователя не существует")
    }

    fun getLastMsgInChat(userId: Int): String {
        val user = checkUser(userId)
        if (user != errorNotFound) {
            val filterChat = chats.filter { it.user1 == users[user] || it.user2 == users[user] }
            var listChats = String()
            for (i in filterChat) {
                listChats += "Chat: id${i.id}  ${i.user1}  ${i.user2}" +
                        if (i.messages.isEmpty()) " no message" else " ${i.messages.last()}" + "\n"
            }
            return listChats
        }
        throw NotFoundException("Пользователя не существует")
    }

    fun getListMsg(chatId: Int, lastMsgId: Int, amountMsg: Int): List<Messages> {
        val indexMsg = chats[chatId].messages.indexOf(chats[chatId].messages.find { it.id == lastMsgId })
        val range = indexMsg until
                if (indexMsg.plus(amountMsg) > chats[chatId].messages.size)
                    chats[chatId].messages.size else indexMsg.plus(amountMsg)
        for (i in range) {
            chats[chatId].messages[i].readIt = true
        }
        return chats[chatId].messages.slice(range)
    }

    fun readMessage(chatId: Int, messageId: Int) {
        val indexChat = chats.indexOf(chats.find { it.id == chatId })
        if (indexChat == errorNotFound) {
            throw NotFoundException("Чат отсутствует")
        } else {
            val indexMsg = chats[indexChat].messages.indexOf(chats[indexChat].messages.find { it.id == messageId })
            if (indexMsg == errorNotFound) {
                throw NotFoundException("Сообщение отсутствует")
            } else {
                chats[indexChat].messages[messageId].readIt = true
            }
        }
    }

    fun delMessage(chatId: Int, messageId: Int): Boolean {
        val indexChat = chats.indexOf(chats.find { it.id == chatId })
        if (indexChat == errorNotFound) {
            throw NotFoundException("Чат отсутствует")
        } else {
            val indexMsg = chats[indexChat].messages.indexOf(chats[indexChat].messages.find { it.id == messageId })
            if (indexMsg < 0) {
                throw NotFoundException("Сообщение отсутствует")
            } else {
                return chats[indexChat].messages.remove(chats[indexChat].messages[messageId])
            }
        }
    }

    fun delChat(chatId: Int): Boolean {
        val indexChat = chats.indexOf(chats.find { it.id == chatId })
        if (indexChat < 0) {
            throw NotFoundException("Чат отсутствует")
        } else {
            return chats.remove(chats[chatId])
        }
    }

    private fun checkUser(userId: Int): Int {
        if (!users.none { it.id == userId }) {
            return users.indexOf(users.find { it.id == userId })
        }
        return errorNotFound
    }

    private fun checkChat(userId1: Int, userId2: Int): Int {
        val user1 = users.find { it.id == userId1 }
        val user2 = users.find { it.id == userId2 }
        val chat = chats.find { it.user1 == user1 && it.user2 == user2 || it.user1 == user2 && it.user2 == user1 }

        if (chat != null) {
            return chats.indexOf(chats.find {
                it.user1 == user1 && it.user2 == user2
                        || it.user1 == user2 && it.user2 == user1
            })
        }
        return errorNotFound
    }

}