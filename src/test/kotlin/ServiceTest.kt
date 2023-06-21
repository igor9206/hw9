import org.junit.Test

import org.junit.Assert.*

class ServiceTest {

    @Test
    fun addUser() {
        val service = Service()
        val result = service.addUser("Igor")

        assertTrue(result)
    }

    @Test
    fun sendMessage() {
        val service = Service()
        service.addUser("Igor")
        service.addUser("Oleg")
        val result = service.sendMessage(0, 1, "Hi")

        assertTrue(result)
    }

    @Test(expected = PostNotFoundException::class)
    fun sendMessageNoUser() {
        val service = Service()
        service.addUser("Igor")
        service.sendMessage(0, 1, "Hi")
    }

    @Test
    fun getUnreadChatsCount() {
        val service = Service()
        service.addUser("Igor")
        service.addUser("Oleg")
        service.addUser("Ira")
        service.sendMessage(0, 1, "Hi")
        service.sendMessage(2, 0, "Hi")
        val result = service.getUnreadChatsCount(0)

        assertEquals(1, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun getChatsNoUser() {
        val service = Service()
        service.addUser("Igor")
        service.getChats(1)
    }

    @Test(expected = PostNotFoundException::class)
    fun getLastMsgInChatNoUser() {
        val service = Service()
        service.addUser("Igor")
        service.getLastMsgInChat(1)
    }

    @Test
    fun getListMsg() {
        val service = Service()
        service.addUser("Igor")
        service.addUser("Oleg")
        service.sendMessage(0, 1, "Hi")
        service.sendMessage(1, 0, "Hi")
        val result = service.getListMsg(0, 0, 2)
        val expected = listOf<Messages>(
            Messages(id = 0, text = "Hi", sender = Users(id = 0, name = "Igor"), readIt = true),
            Messages(id = 1, text = "Hi", sender = Users(id = 1, name = "Oleg"), readIt = true)
        )

        assertEquals(expected, result)
    }

    @Test(expected = PostNotFoundException::class)
    fun readMessageNoUser() {
        val service = Service()
        service.addUser("Igor")
        service.addUser("Oleg")
        service.sendMessage(0, 1, "Hi")
        service.readMessage(1, 0)
    }

    @Test
    fun delMessage() {
        val service = Service()
        service.addUser("Igor")
        service.addUser("Oleg")
        service.sendMessage(0, 1, "Hi")
        val result = service.delMessage(0, 0)

        assertTrue(result)
    }

    @Test
    fun delChat() {
        val service = Service()
        service.addUser("Igor")
        service.addUser("Oleg")
        service.sendMessage(0, 1, "Hi")
        val result = service.delChat(0)

        assertTrue(result)
    }
}