package interactions.actions

import Tickets.Companion.InventoryManager
import interactions.Action
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import utility.staff

object Pick : Action("pick", "tickets.staff.pick", true) {
    private fun main(commandSender: CommandSender, ticket: Ticket) {
        ticket.addMessage(commandSender, "Picked")
        ticket.notify(commandSender.name + " has picked your ticket")

        staff(commandSender.name + " has picked " + ticket.holdersName() + "'s ticket")
    }

    fun gui(player: Player, ticket: Ticket) {
        if (!hasPerms(player)) return

        main(player, ticket)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        val ticket = parseInput(commandSender, input) ?: return

        main(commandSender, ticket)
    }
}