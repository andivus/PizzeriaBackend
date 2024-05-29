package me.dev.database

interface DatabaseConnector {
    fun connect()
    fun close()
}