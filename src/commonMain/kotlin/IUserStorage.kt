interface IUserStorage {
    fun getUser(name: String): User?
    fun addUser(user: User)
    fun removeUser(name: String)
    fun listUsers(): List<User>
}