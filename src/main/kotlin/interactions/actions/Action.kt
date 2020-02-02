package interactions.actions

import Tickets.Companion.ChatInput
import Tickets.Companion.TicketManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import utility.message
import java.util.*

abstract class Action(override val name: String, override val permission: String, override val targets: Boolean) : Command {
    abstract fun cli(commandSender: CommandSender, input: String)

    open fun response(player: Player, message: String) {}

    fun getInput(player: Player) {
        ChatInput.register(player, this)
        player.closeInventory()
    }

    fun target(message: String) = if (message.isBlank()) null else Bukkit.getOfflinePlayer(message.split(" ")[0])

    fun ticket(uuid: UUID, input: String) = TicketManager[uuid, input.split(" ").last().toIntOrNull() ?: 1]

    fun retrieveTicket(commandSender: CommandSender, message: String): Ticket? {
        println(">" + message.split(" ")[0] + "<")
        val target = target(message)

        return if (target == null) {
            commandSender.message("That player does not exist")
            null
        } else {
            val ticket = ticket(target.uniqueId, message)
            if (ticket == null) commandSender.message("Ticket not found")

            ticket
        }
    }
}