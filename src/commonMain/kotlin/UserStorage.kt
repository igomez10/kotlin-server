class UserStorage: IUserStorage {
    private var users: List<User> = listOf();
    override fun getUser(name: String): User? {
        for (user in users) {
            if (user.name == name) {
                return user
            }
        }
        return null
    }

    override fun addUser(user: User) {
        users += user
    }

    override fun removeUser(name : String) {
        // Remove user from list
        for (user in users) {
            if (user.name == name) {
                users -= user
            }
        }

    }

    override fun listUsers(): List<User> {
        return users
    }
}