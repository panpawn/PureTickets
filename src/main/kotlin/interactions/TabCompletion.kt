package interactions

import Tickets.Companion.TicketManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import storage.TicketSQL
import java.util.*

object TabCompletion : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return when (args.size) {
            1 -> Actions.values()
                .filter { !it.isAlias }
                .filter { sender.hasPermission(it.action.permission) }
                .map { it.name.toLowerCase() }
                .filter { it.startsWith(args[0].toLowerCase()) }
                .toMutableList()

            2 -> if (Actions.find(args[0].toUpperCase())?.action?.targets == true)
                TicketSQL.recentPlayers()
                .mapNotNull { Bukkit.getOfflinePlayer(it).name }
                .filter { it.startsWith(args[1], true) }
                .toMutableList()
            else mutableListOf()

            3 -> if (Actions.find(args[0].toUpperCase())?.action?.targets == true)
                TicketManager[UUID.fromString(args[1])]?.indices?.toList()
                    ?.map { (it + 1).toString() }
                    ?.toMutableList() ?: mutableListOf()
            else mutableListOf()

            else -> mutableListOf()
        }
    }
}