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
        statement.execute("CREATE TABLE IF NOT EXISTS user (uuid INTEGER, points INTEGER, blocked INTEGER);")

        statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS ticket (id INTEGER, uuid TEXT, picker TEXT, status TEXT, x INTEGER, y INTEGER, z Integer, world TEXT);")

        statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS message (ticketId INTEGER, uuid TEXT, message TEXT, date INTEGER);")
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