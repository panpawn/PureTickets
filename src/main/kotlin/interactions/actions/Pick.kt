package interactions.actions

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus
import utility.staff

object Pick : Action("pick", "tickets.staff", true) {
    private fun main(commandSender: CommandSender, ticket: Ticket) {
        val uuid = if (commandSender is Player) {
            ticket.setStatus(TicketStatus.PICKED, commandSender.uniqueId)
            commandSender.uniqueId
        } else {
            ticket.setStatus(TicketStatus.PICKED)
            null
        }

        ticket.addMessage(uuid, "Picked")
        ticket.notify(commandSender.name + " has picked your ticket")
        staff(commandSender.name + " has picked " + ticket.holdersName() + "'s ticket")
    }

    fun gui(player: Player, ticket: Ticket) = main(player, ticket)

    override fun cli(commandSender: CommandSender, input: String) {
        val ticket = retrieveTicket(commandSender, input) ?: return

        main(commandSender, ticket)
    }
}