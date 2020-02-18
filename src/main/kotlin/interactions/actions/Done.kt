package interactions.actions

import Tickets.Companion.InventoryManager
import interactions.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus
import utility.message

object Done : Action("done", "tickets.staff.done",  true) {
    fun gui(player: Player, ticket: Ticket): String? {
        if (!hasPerms(player))
            return "You do not have permission for this"
        if (player.uniqueId != ticket.picker)
            return "You cannot done-mark a ticket that someone else has picked"

        ticket.setStatus(TicketStatus.CLOSED)
        InventoryManager.refresh(player)

        return null
    }

    override fun cli(commandSender: CommandSender, input: String) {
        val ticket = parseInput(commandSender, input) ?: return

        ticket.setStatus(TicketStatus.CLOSED)
        commandSender.message("Ticket closed")
    }
}