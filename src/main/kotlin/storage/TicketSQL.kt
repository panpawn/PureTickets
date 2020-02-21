package storage

import Tickets.Companion.SQLManager
import managers.PlayerManager
import ticket.Message
import ticket.Ticket
import ticket.TicketStatus
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

object TicketSQL {
    fun currentId() = "SELECT MAX(id) FROM ticket".query { it }.getInt(1)

    fun count(uuid: UUID, status: TicketStatus) = "SELECT COUNT(id) FROM ticket WHERE uuid = ? AND status = ?".query {
        it.setUUID(1, uuid)
        it.setString(2, status.name)
        it
    }.getInt(1)

    fun recentPlayers() = "SELECT DISTINCT uuid FROM ticket WHERE id IN (SELECT DISTINCT ticketId FROM message ORDER BY date DESC)".query { it }.forEachSequence { it.getUUID("uuid") }

    fun insTicket(ticket: Ticket) {
        "INSERT INTO ticket(id, uuid, picker, status) VALUES (?, ?, ?, ?)".run {
            it.setInt(1, ticket.id)
            it.setString(2, ticket.uuid.toString())
            it.setString(3, ticket.picker.toString())
            it.setString(4, ticket.status.name)

            it
        }
    }

    fun insMessage(id: Int, message: Message) {
        "INSERT INTO message(ticketId, uuid, message, date) VALUES (?, ? ,? ,?)".run {
            it.setInt(1, id)
            it.setString(2, message.uuid.toString())
            it.setString(3, message.message)
            it.setLong(4, message.date.atZone(ZoneId.systemDefault()).toEpochSecond())

            it
        }
    }

    fun update(ticket: Ticket) {
        "UPDATE ticket SET picker = ?, status = ? WHERE id = ?".run {
            it.setString(1, ticket.picker.toString())
            it.setString(2, ticket.status.name)
            it.setInt(3, ticket.id)

            it
        }
    }

    fun playerProfileExists(uuid: UUID) = "SELECT EXISTS(SELECT 1 FROM player WHERE uuid = ?)".query {
        it.setUUID(1, uuid)
        it
    }.getBoolean(1)

    fun getPlayerProfile(uuid: UUID): PlayerManager.PlayerProfile {
        "SELECT points, blocked FROM user WHERE uuid = ?".query {
            it.setUUID(1, uuid)
            it
        }.also {
            return PlayerManager.PlayerProfile(uuid, it.getInt(1), it.getBoolean(2))
        }
    }

    fun insPlayerProfile(playerProfile: PlayerManager.PlayerProfile) {
        "INSERT INTO user(uuid, points, blocked) values (?, ?)".run {
            it.setInt(1, playerProfile.getPoints())
            it.setBoolean(2, playerProfile.isBlocked())
            it
        }
    }

    fun updPlayerProfile(playerProfile: PlayerManager.PlayerProfile) {
        "UPDATE user SET points = ?, blocked = ? WHERE uuid = ?".run {
            it.setInt(1, playerProfile.getPoints())
            it.setBoolean(2, playerProfile.isBlocked())
            it.setUUID(3, playerProfile.uuid)
            it
        }
    }

    fun getActive(): HashMap<UUID, ArrayList<Ticket>> {
        val tickets = HashMap<UUID, ArrayList<Ticket>>()

        "SELECT * FROM ticket WHERE status <> ?".query {
            it.setString(1, "CLOSED")
            it
        }.forEach { ticket ->
            val messages = getMessages(ticket)

            val finalTicket = Ticket(ticket.getInt("id"), ticket.getUUID("uuid")!!, ticket.getUUID("picker"), messages, TicketStatus.valueOf(ticket.getString("status")))

            tickets.putIfAbsent(finalTicket.uuid, ArrayList())
            tickets[finalTicket.uuid]?.add(finalTicket)
        }

        return tickets
    }

    fun getInactive(uuid: UUID): ArrayList<Ticket> {
        val tickets = ArrayList<Ticket>()

        "SELECT * FROM ticket WHERE status = ? AND uuid = ?".query {
            it.setString(1, "CLOSED")
            it.setUUID(2, uuid)
            it
        }.forEach { ticket ->
            val finalTicket = Ticket(ticket.getInt("id"), uuid, ticket.getUUID("picker"), getMessages(ticket), TicketStatus.valueOf(ticket.getString("status")))

            tickets.add(finalTicket)
        }

        return tickets
    }

    private fun getMessages(ticket: ResultSet): ArrayList<Message> {
        return "SELECT * FROM message WHERE ticketId = ?".query {
            it.setInt(1, ticket.getInt("id"))
            it
        }.forEachSequence { data ->
            Message(data.getUUID("uuid")!!, data.getString("message"), LocalDateTime.ofInstant(Instant.ofEpochMilli(data.getLong("date")), ZoneId.systemDefault()))
        }.toMutableList() as ArrayList<Message>
    }

    private fun ResultSet.getUUID(column: String) = if (getString(column) == "null") null else UUID.fromString(getString(column))

    private fun PreparedStatement.setUUID(index: Int, uuid: UUID) = setString(index, uuid.toString())

    private fun String.run(args: (PreparedStatement) -> PreparedStatement) {
        val c = SQLManager.getConnection()
        val s = c.prepareStatement(this)

        args.invoke(s).execute()
    }

    private fun String.query(args: (PreparedStatement) -> PreparedStatement): ResultSet {
        val c = SQLManager.getConnection()
        val s = c.prepareStatement(this)

        return args.invoke(s).executeQuery()
    }

    private fun <E> ResultSet.forEach(process: (ResultSet) -> E) {
        while (next()) process.invoke(this)
    }

    private fun <T> ResultSet.forEachSequence(process: (ResultSet) -> T?) = sequence {
        while (next()) process.invoke(this@forEachSequence)?.also { yield(it) }
    }.toList()
}