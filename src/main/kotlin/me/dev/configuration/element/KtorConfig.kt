package me.dev.configuration.element

data class DeploymentConfig(val port: Int)
data class ServerConfig(val isDevelopment: Boolean)

data class DatabaseConfig (
    val driverClass: String,
    val url: String,
    val user: String,
    val password: String,
    val maxPoolSize: Int,
)

class KtorConfig {
    var development: Boolean = true
    lateinit var serverConfig: ServerConfig
    lateinit var deploymentConfig: DeploymentConfig
    lateinit var databaseConfig: DatabaseConfig
}