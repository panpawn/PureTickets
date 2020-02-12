package ticket

import Tickets.Companion.TicketManager
import managers.Notifications
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
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
        addMessage(player, message)
        TicketSQL.insTicket(this)
    }

    fun addMessage(s: CommandSender?, message: String) {
        val msg = Message(if (s is Player) s.uniqueId else null, message)
        messages += msg

        TicketSQL.insMessage(id, msg)
    }

    fun currentMessage() = messages.last { it.uuid == uuid }.message

    fun setStatus(ticketStatus: TicketStatus, pickUser: CommandSender? = null) {
        status = ticketStatus

         when(ticketStatus) {
            TicketStatus.OPEN -> picker =  null

            TicketStatus.CLOSED -> TicketManager.remove(uuid, this)

            TicketStatus.PICKED -> {
                picker = if (pickUser !is Player) null
                else pickUser.uniqueId
            }
        }

        TicketSQL.update(this)
    }

    fun notify(message: String) = Notifications.send(uuid, message)

    fun holdersName() = Bukkit.getOfflinePlayer(uuid).name
}

data class Message(val uuid: UUID?, val message: String, val date: LocalDateTime = LocalDateTime.now())

enum class TicketStatus { OPEN, PICKED, CLOSED }