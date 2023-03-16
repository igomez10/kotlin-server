import org.junit.jupiter.api.Test

class UserStorageTest {
    @Test
    fun test() {
        val userStorage = UserStorage()
        val user = User(0, "John", 20)
        userStorage.addUser(user)
        val loadedUser = userStorage.getUserById(0)
        assert(loadedUser == user)
    }
}

