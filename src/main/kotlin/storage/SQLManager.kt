package storage

import Tickets.Companion.TICKETS
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class SQLManager {
    private val db = File(TICKETS.dataFolder, "tickets.db")
    private var database: Connection? = null

    init {
        if (!db.exists())
            db.createNewFile()

        run()
    }

    private fun run() {
        val connection = this.getConnection()
        var statement: Statement

        statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS ticket (id INTEGER, uuid TEXT, picker TEXT, status TEXT);")

        statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS message (ticketId INTEGER, uuid TEXT, message TEXT, date INTEGER);")

        statement = connection.createStatement()
        val resultSet = statement.executeQuery("PRAGMA user_version")
        val version = resultSet.getInt("user_version")
        resultSet.close()

        println("SQL Initialised | User Version: $version")
    }

    fun getConnection(): Connection {
        if (database == null) {
            Class.forName("org.sqlite.JDBC")
            database = DriverManager.getConnection("jdbc:sqlite:$db")
        }
        return database!!
    }

    fun close() {
        database?.close()
    }
}