interface IUserStorage {
    fun getUser(name: String): User?
    fun getUserById(id: Int): User?
    fun addUser(user: User)
    fun removeUser(name: String)
    fun removeUserById(id: Int)
    fun listUsers(): List<User>
}