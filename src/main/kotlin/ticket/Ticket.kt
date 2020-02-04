package ticket

import Tickets.Companion.TicketManager
import managers.Notifications
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import storage.TicketSQL
import utility.message
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class Ticket(val id: Int, val uuid: UUID, picker: UUID?, private val messages: ArrayList<Message>, status: TicketStatus) {
    var picker = picker
        private set
    var status = status
        private set

    constructor(player: Player, message: String) : this(TicketManager.currentId, player.uniqueId, null, ArrayList(), TicketStatus.OPEN) {
        addMessage(player.uniqueId, message)
        TicketSQL.insTicket(this)
    }

    fun addMessage(uuid: UUID?, message: String) {
        val msg = Message(uuid, message)
        messages += msg

        TicketSQL.insMessage(id, msg)
    }

    fun currentMessage() = messages.last { it.uuid == uuid }.message

    fun setStatus(ticketStatus: TicketStatus, pickPlayer: UUID? = null) {
        status = ticketStatus
        if (pickPlayer != null) picker = pickPlayer

        if (status == TicketStatus.CLOSED)
            TicketManager.remove(uuid, this)

        if (status == TicketStatus.OPEN) {
            picker = null
        }

        TicketSQL.update(this)
    }

    fun notify(message: String) = Notifications.send(uuid, message)

    fun holdersName() = Bukkit.getOfflinePlayer(uuid).name
}

data class Message(val uuid: UUID?, val message: String, val date: LocalDateTime = LocalDateTime.now())

enum class TicketStatus { OPEN, PICKED, CLOSED }