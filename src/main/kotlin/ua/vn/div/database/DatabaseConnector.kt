package ua.vn.div.database

interface DatabaseConnector {
    fun connect()
    fun close()
}