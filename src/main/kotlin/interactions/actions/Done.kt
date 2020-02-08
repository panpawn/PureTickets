package interactions.actions

import org.bukkit.command.CommandSender

object Done : Action("done", "tickets.user.done",  true) {
    override fun cli(commandSender: CommandSender, input: String) {

    }
}