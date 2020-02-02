package interactions

import interactions.menus.Start
import utility.message
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Dispatcher : CommandExecutor {
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
}