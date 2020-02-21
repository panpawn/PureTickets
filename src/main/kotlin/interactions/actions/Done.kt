package interactions.actions

import Tickets.Companion.InventoryManager
import Tickets.Companion.PlayerManager
import interactions.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus
import utility.message

object Done : Action("done", "tickets.staff.done",  true) {
    override fun menu(player: Player, ticket: Ticket?) {
        if (!hasPerms(player))
            throw Exception("You do not have permission for this")
        if (player.uniqueId != ticket?.picker)
            throw Exception( "You cannot done-mark a ticket that someone else has picked")

        ticket.setStatus(TicketStatus.CLOSED)

        PlayerManager[ticket.picker]?.incrementPoints()
        InventoryManager.refresh(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        val ticket = parseInput(commandSender, input) ?: return

        ticket.setStatus(TicketStatus.CLOSED)
        commandSender.message("Ticket closed")
    }
}