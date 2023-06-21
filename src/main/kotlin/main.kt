class NotFoundException(message: String) : RuntimeException(message)

data class Chats(
    val id: Int = -1,
    val user1: Users,
    val user2: Users,
    var messages: MutableList<Messages> = mutableListOf()
)

data class Messages(
    val id: Int = -1,
    val text: String,
    val sender: Users,
    var readIt: Boolean = false
)

data class Users(
    val id: Int = -1,
    val name: String
)