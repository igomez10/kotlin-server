package me.ignacio.application

import IUserStorage
import User
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val iUserStorage: IUserStorage, private var counter: Int = 0) {
    @GetMapping("/users")
    fun getUser(): String {
        return iUserStorage.listUsers().toString()
    }

    @PostMapping("/users")
    fun addUser(@RequestBody user: User): String {
        iUserStorage.addUser(user)
        return "User added"
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Int): User? {
        return iUserStorage.getUserById(id)
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Int): String {
        iUserStorage.removeUserById(id)
        return "User deleted"
    }


    fun increaseCounter() {
        counter++
    }

}
