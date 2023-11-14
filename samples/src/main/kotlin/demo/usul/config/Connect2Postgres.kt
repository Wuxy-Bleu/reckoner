package demo.usul.config

import io.ktor.server.application.*
import io.ktor.server.config.yaml.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (embedded) {
        return DriverManager.getConnection("jdbc:postgresql://localhost/test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val configs = YamlConfig("postgres.yaml")
        val url = "jdbc:postgresql://localhost:5432/" +
                configs?.property("services.postgres.environment.POSTGRES_DB")?.getString()
        val user = configs?.property("services.postgres.environment.POSTGRES_USER")?.getString()
        val password = configs?.property("services.postgres.environment.POSTGRES_PASSWORD")?.getString()
        return DriverManager.getConnection(url, user, password)
    }
}