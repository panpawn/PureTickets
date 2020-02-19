package interactions

import Tickets.Companion.ChatInput
import Tickets.Companion.TicketManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import utility.getOfflinePlayer
import utility.message

abstract class Action(override val name: String, override val permission: String, override val targets: Boolean) : Command {
    abstract fun cli(commandSender: CommandSender, input: String)

    open fun response(player: Player, message: String) {}

    fun getInput(player: Player) {
        ChatInput.register(player, this)
        player.closeInventory()
    }

    fun parseInput(commandSender: CommandSender, message: String) = TicketManager[getOfflinePlayer(message.split(" ").getOrNull(0))?.uniqueId, message.split(" ").last().toIntOrNull() ?: 1].also {
        if (it == null) commandSender.message("Ticket not found")
    }

    fun hasPerms(player: Player) = player.hasPermission(permission)
}