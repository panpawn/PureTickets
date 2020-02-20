package interactions.actions

import Tickets.Companion.InventoryManager
import interactions.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus
import utility.staff

object Pick : Action("pick", "tickets.staff.pick", true) {
    private fun main(commandSender: CommandSender, ticket: Ticket?) {
        ticket?.setStatus(TicketStatus.PICKED, commandSender)
        ticket?.addMessage(commandSender, "Picked")
        ticket?.notify(commandSender.name + " has picked your ticket")

        staff(commandSender.name + " has picked " + ticket?.holdersName() + "'s ticket")
    }

    override fun menu(player: Player, ticket: Ticket?) {
        if (!hasPerms(player))
            throw Exception("You do not have permission for this")

        main(player, ticket)
        InventoryManager.refresh(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        val ticket = parseInput(commandSender, input) ?: return

        main(commandSender, ticket)
    }
}