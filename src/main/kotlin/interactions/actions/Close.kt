package interactions.actions

import Tickets.Companion.InventoryManager
import Tickets.Companion.TicketManager
import interactions.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus
import utility.message

object Close : Action("close", "tickets.user.close", false) {
    fun gui(player: Player, ticket: Ticket) {
        if (!hasPerms(player)) return

        ticket.setStatus(TicketStatus.CLOSED)
        InventoryManager.refresh(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        if (commandSender !is Player) {
            commandSender.message("Console cannot player commands.")
            return
        }

        val ticket = TicketManager[commandSender.uniqueId]?.getOrNull(input.split(" ").last().toIntOrNull() ?: 1)

        if (ticket != null) {
            ticket.setStatus(TicketStatus.CLOSED)
            commandSender.message("Ticket closed")
        } else {
            commandSender.message("Cannot find ticket")
        }
    }
}