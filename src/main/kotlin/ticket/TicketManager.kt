package ticket

import storage.TicketSQL
import java.util.*
import kotlin.collections.ArrayList

class TicketManager {
    var currentId = TicketSQL.currentId()
        get() {
            field++
            return field
        }

    private val tickets = TicketSQL.getActive()

    operator fun get(uuid: UUID?, index: Int) = tickets[uuid]?.get(index - 1)

    operator fun get(uuid: UUID) = tickets[uuid]

    fun add(uuid: UUID, ticket: Ticket) {
        tickets.putIfAbsent(uuid, ArrayList())
        tickets[uuid]?.add(ticket)
    }

    fun remove(uuid: UUID, ticket: Ticket) {
        tickets[uuid]?.remove(ticket)
    }

    fun countNot(status: TicketStatus) = tickets.values.flatten().count { it.status != status }
}