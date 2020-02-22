package interactions.actions

import Tickets.Companion.InventoryManager
import Tickets.Companion.TicketManager
import interactions.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus
import utility.message

object Reopen : Action("reopen", "tickets.user.reopen", false) {
    override fun menu(player: Player, ticket: Ticket?) {
        if (player.uniqueId == ticket?.uuid) {
            ticket.setStatus(TicketStatus.OPEN)
            ticket.notify("You have reopened your ticket")
        } else {
            ticket?.setStatus(TicketStatus.PICKED, player)
            ticket?.notify(player.name + " has reopened and picked your ticket")
        }

        TicketManager.add(ticket!!.uuid, ticket)
        InventoryManager.refresh(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        val split = input.split(" ")

        if (commandSender is Player && split.size == 1 && split[0].toIntOrNull() != null) {
            val ticket = TicketManager[commandSender.uniqueId]?.getOrNull(input.split(" ").last().toIntOrNull() ?: 1)

            if (ticket != null) {
                ticket.setStatus(TicketStatus.OPEN)
                ticket.notify("You have reopened your ticket")
            } else {
                commandSender.message("Cannot find ticket")
            }
        } else {
            val ticket = parseInput(commandSender, input) ?: return

            ticket.setStatus(TicketStatus.PICKED, commandSender)
            ticket.notify(commandSender.name + " has reopened and picked your ticket")
        }
    }
}