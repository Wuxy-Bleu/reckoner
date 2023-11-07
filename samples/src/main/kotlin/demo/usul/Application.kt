package demo.usul

import demo.usul.plugins.configureRouting
import demo.usul.plugins.configureSerialization
import demo.usul.plugins.configureTemplating
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureTemplating()
    configureRouting()
}
