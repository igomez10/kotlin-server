package me.ignacio.application

import IUserStorage
import UserStorage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun userStorage(): IUserStorage {
        return UserStorage()
    }
}