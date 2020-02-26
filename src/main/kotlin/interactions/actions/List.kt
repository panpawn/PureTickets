package interactions.actions

import Tickets.Companion.TICKETS
import Tickets.Companion.TicketManager
import interactions.Action
import interactions.menus.ViewTickets
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import utility.message

object List : Action("list", "tickets.staff.list", false) {
    override fun menu(player: Player, ticket: Ticket?) {
        player.message("Enter the name of the player you want to view:")
        getInput(player)
    }

    override fun cli(commandSender: CommandSender, input: String) {
        TicketManager.all().forEach {
            commandSender.message(it.currentMessage(), false)
        }
    }

    override fun response(player: Player, message: String) {
        val op = Bukkit.getOfflinePlayer(message)

        if (!TicketManager.contains(op)) {
            player.message("Cannot find active tickets for $message")
            return
        }

        Bukkit.getScheduler().runTask(TICKETS, Runnable{
            ViewTickets(player) {
                TicketManager[op.uniqueId]
            }.show()
        })
    }
}