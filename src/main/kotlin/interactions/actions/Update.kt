package interactions.actions

import Tickets.Companion.TicketManager
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import utility.message
import utility.staff

object Update : Action("update", "tickets.user.update", false) {
    var ticket: Ticket? = null

    fun gui(player: Player, input: Ticket) {
        if (!hasPerms(player)) return

        ticket = input
        player.message("Enter the updated message")
        getInput(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        if (commandSender !is Player) {
            commandSender.message("Console cannot update a ticket")
            return
        }

        commandSender.message("Enter the updated message")
        ticket = TicketManager[commandSender.uniqueId]?.last()
        getInput(commandSender)
    }

    override fun response(player: Player, message: String) {
        if (ticket == null) {
            player.message("Ticket not found")
            return
        }

        ticket?.addMessage(player, message)
        player.message("Ticket updated successfully")

        staff("§f§l" + player.name + "§7 - " + message)
    }
}