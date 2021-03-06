package co.uk.magmo.puretickets.ticket

import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.utils.asUUID
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID

class TicketManager {
    private val tickets = SQLFunctions.retrieveAllTickets()
    private var current = SQLFunctions.currentTicketId()

    operator fun get(uuid: UUID?, id: Int): Ticket? = tickets[uuid].first { ticket -> ticket.id == id }

    operator fun get(uuid: UUID?): MutableList<Ticket> = tickets[uuid]

    operator fun contains(player: OfflinePlayer) = tickets.containsKey(player.uniqueId)

    fun asMap(): MutableMap<UUID, MutableCollection<Ticket>> = tickets.asMap()

    fun allKeys(): MutableSet<UUID> = tickets.keySet()

    fun createTicket(player: Player, message: Message): Ticket {
        current += 1
        return Ticket(current, player.uniqueId, arrayListOf(message), TicketStatus.OPEN, null, player.location).also {
            tickets.put(player.uniqueId, it)

            SQLFunctions.insertTicket(it)
            SQLFunctions.insertMessage(it, message)
        }
    }

    fun update(information: TicketInformation, message: Message): Ticket? {
        val ticket = tickets[information.player].first { ticket -> ticket.id == information.index }
        val index = tickets[information.player].indexOf(ticket)

        ticket.addMessageAndUpdate(message)
        tickets[information.player][index] = ticket

        return ticket
    }

    fun pick(uuid: UUID?, information: TicketInformation): Ticket? {
        val ticket = tickets[information.player].first { ticket -> ticket.id == information.index }
        val index = tickets[information.player].indexOf(ticket)
        val message = Message(MessageReason.PICKED, null, uuid)

        ticket.status = TicketStatus.PICKED
        ticket.pickerUUID = uuid
        ticket.addMessageAndUpdate(message)

        tickets[information.player][index] = ticket
        return ticket
    }

    fun yield(uuid: UUID?, information: TicketInformation): Ticket? {
        val ticket = tickets[information.player].first { ticket -> ticket.id == information.index }
        val index = tickets[information.player].indexOf(ticket)
        val message = Message(MessageReason.YIELDED, null, uuid)

        ticket.status = TicketStatus.OPEN
        ticket.pickerUUID = null
        ticket.addMessageAndUpdate(message)

        tickets[information.player][index] = ticket

        return ticket
    }

    fun close(uuid: UUID?, information: TicketInformation): Ticket? {
        val ticket = tickets[information.player].first { ticket -> ticket.id == information.index }
        val message = Message(MessageReason.CLOSED, null, uuid)

        tickets.remove(information.player, ticket)
        ticket.status = TicketStatus.CLOSED
        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun done(uuid: UUID?, information: TicketInformation): Ticket? {
        val ticket = tickets[information.player].first { ticket -> ticket.id == information.index }
        val message = Message(MessageReason.DONE_MARKED, null, uuid)

        tickets.remove(information.player, ticket)
        ticket.status = TicketStatus.CLOSED
        ticket.addMessageAndUpdate(message)

        return ticket
    }

    fun reopen(uuid: UUID?, information: TicketInformation): Ticket {
        val ticket = SQLFunctions.retrieveSingleTicket(information.index)
        val message = Message(MessageReason.REOPENED, null, uuid)

        ticket.status = TicketStatus.OPEN
        ticket.addMessageAndUpdate(message)

        tickets.put(information.player, ticket)

        return ticket
    }

    private fun Ticket.addMessageAndUpdate(message: Message) {
        messages.add(message)
        SQLFunctions.insertMessage(this, message)
        SQLFunctions.updateTicket(this)
    }
}