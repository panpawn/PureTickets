package interactions.actions

import Tickets.Companion.InventoryManager
import Tickets.Companion.TicketManager
import utility.message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import utility.staff

object Create : Action("create", "tickets.user.create", false) {
    private fun main(player: Player, message: String) {
        if (message.length < 4) {
            player.message("Ticket message must be longer than 3 characters")
            return
        }

        TicketManager.add(player.uniqueId, Ticket(player, message))
        player.message("Ticket added successfully")
        staff("§f§l" + player.name + "§7 - " + message)
    }

    fun gui(player: Player) {
        if (!hasPerms(player)) return

        player.message("Enter your ticket message")
        getInput(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        if (commandSender !is Player) {
            commandSender.message("Console cannot player commands.")
            return
        }

        main(commandSender, input)
    }

    override fun response(player: Player, message: String) {
        main(player, message)
        InventoryManager.refresh(player)
    }
}