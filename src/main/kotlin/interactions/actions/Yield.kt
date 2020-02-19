package interactions.actions

import Tickets.Companion.InventoryManager
import interactions.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus

object Yield : Action("yield", "tickets.staff.yield", true) {
    fun gui(player: Player, ticket: Ticket): String? {
        if (!hasPerms(player))
            return "You do not have permission"

        ticket.setStatus(TicketStatus.OPEN)
        ticket.notify(player.name + " has yielded your ticket")
        InventoryManager.refresh(player)

        return null
    }

    override fun cli(commandSender: CommandSender, input: String) {

    }
}