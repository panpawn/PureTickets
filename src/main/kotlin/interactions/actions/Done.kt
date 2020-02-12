package interactions.actions

import Tickets.Companion.InventoryManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus
import utility.message

object Done : Action("done", "tickets.staff.done",  true) {
    fun gui(player: Player, ticket: Ticket) {
        if (!hasPerms(player)) return

        ticket.setStatus(TicketStatus.CLOSED)
        InventoryManager.refresh(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        val ticket = parseInput(commandSender, input) ?: return

        ticket.setStatus(TicketStatus.CLOSED)
        commandSender.message("Ticket closed")
    }
}