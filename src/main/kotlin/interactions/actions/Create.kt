package interactions.actions

import Tickets.Companion.TicketManager
import utility.message
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import utility.staff

object Create : Action("create", "tickets.user", false) {
    private fun main(player: Player, message: String) {
        TicketManager.add(player.uniqueId, Ticket(player, message))
        player.message("Ticket added successfully")
        staff("§f§l" + player.name + "§7 - " + message)
    }

    fun gui(player: Player) {
        player.message("Enter your ticket message")
        getInput(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        if (commandSender is ConsoleCommandSender) {
            commandSender.message("Console cannot create tickets.")
            return
        }

        main(commandSender as Player, input)
    }

    override fun response(player: Player, message: String) {
        main(player, message)
    }
}