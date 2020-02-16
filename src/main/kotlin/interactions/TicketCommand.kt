package interactions

import Tickets.Companion.TicketManager
import interactions.menus.Start
import org.bukkit.Bukkit
import utility.message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import storage.TicketSQL
import java.util.*

object TicketCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, cmd: Command, commmandlabel: String, args: Array<String>): Boolean {
        if (args.isEmpty() && sender is Player) {
            Start(sender).show()
            return true
        }

        val name = args[0].toUpperCase()
        if (Actions.values().any { it.name == name}) {
            val command = Actions.valueOf(name).action

            if (!sender.hasPermission(command.permission)) {
                sender.message("Locale.noPermission")
                return true
            }

            if (args.size == 1 && command.targets) {
                sender.message("Usage: /ticket ${command.name} [player] <ticket number>")
                return true
            }

            val message = args.drop(1).joinToString(" ")

            command.cli(sender, message)
            return true
        }

        sender.message("Locale.commandDoesntExist")
//        Help(commands).execute(player, "")
        return true
    }

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
                TicketManager[UUID.fromString(args[1])]
                    ?.indices?.toList()
                    ?.map { (it + 1).toString() }
                    ?.toMutableList() ?: mutableListOf()
                else mutableListOf()

            else -> mutableListOf()
        }
    }
}