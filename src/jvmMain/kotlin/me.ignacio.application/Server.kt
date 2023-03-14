package me.ignacio.application

import IUserStorage
import User
import UserStorage
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import java.util.UUID.randomUUID

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/kotlin-server.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::myApplicationModule).start(wait = true)
}

fun Application.configureRouting() {
    val userStorage = UserStorage()
    val controller = Controller( userStorage, 0)

    userStorage.addUser(User(1, "Ignacio", 20))

    intercept(Plugins) {
        // Collect Metrics
        val startTime = System.currentTimeMillis()
        proceed()
        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        println("Request took $elapsedTime ms")
    }
    // add request id header
    intercept(ApplicationCallPipeline.Monitoring) {
        // generate request id uuid
        val requestId = randomUUID().toString()
        // add request id in header
        call.response.header("X-Request-Id", requestId)
        proceed()
    }

    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
        get("/counter") {
            call.respondText {

                var answer = controller.counter.toString()
                controller.increaseCounter().toString()
                return@respondText answer
            }
        }
        get("/users") {
            call.respondText {
                //log users
                println("GET /users")
                var answer = controller.listUsers().toString()
                return@respondText answer
            }
        }
        post("/users") {
            call.respondText {
                println("POST /users")

                // parse body to user from json
                try {
                    val newuser = call.receive<User>()
                    println(newuser.toString())
                    // parse user in body
                    controller.addUser(newuser)
                    // status code created 201
                    call.response.status(HttpStatusCode.Created)
                    return@respondText "User added"
                } catch (e: Exception) {
                    //log error
                    println(e)
                    call.response.status(HttpStatusCode.BadRequest)
                    return@respondText "Bad request"
                }
            }
        }
        get("/users/{name}") {
            call.respondText {
                println("GET /users/{name}")
                // get name from path
                val name = call.parameters["name"]
                // get user from storage
                val user = controller.getUser(name!!)
                // check if user is null
                if (user == null) {
                    // status code not found 404
                    call.response.status(HttpStatusCode.NotFound)
                    return@respondText "User not found"
                } else {
                    // status code ok 200
                    call.response.status(HttpStatusCode.OK)
                    return@respondText user.toString()
                }
            }
        }
        delete("/users/{name}") {
            call.respondText {
                println("DELETE /users/{name}")
                // get name from path
                val name = call.parameters["name"]
                // get user from storage
                val user = controller.getUser(name!!)
                // check if user is null
                if (user == null) {
                    // status code not found 404
                    call.response.status(HttpStatusCode.NotFound)
                    return@respondText "User not found"
                } else {
                    // remove user from storage
                    controller.removeUser(name)
                    // status code ok 200
                    call.response.status(HttpStatusCode.OK)
                    return@respondText "User removed"
                }
            }
        }

    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureLogging() {
    install(CallLogging)
}

fun Application.myApplicationModule() {
    configureRouting()
    configureLogging()
    configureSerialization()
}

//class Controller with property counter of type integer
class Controller(private val iUserStorage: IUserStorage, var counter: Int = 0) {

    fun listUsers(): List<User> {
        return iUserStorage.listUsers()
    }
    fun addUser(user: User) {
        iUserStorage.addUser(user)
    }
    fun getUser(name: String): User? {
        return iUserStorage.getUser(name)
    }
    fun removeUser(name: String) {
        iUserStorage.removeUser(name)
    }

    fun increaseCounter() {
        counter++
    }


}

// method increase counter
fun Controller.increaseCounter() {
    counter++
}


