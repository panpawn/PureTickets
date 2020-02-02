package interactions.actions

import interactions.menus.Start
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ticket.Ticket
import ticket.TicketStatus

object Close : Action("close", "tickets.user", false) {
    fun gui(player: Player, ticket: Ticket) {
        ticket.setStatus(TicketStatus.CLOSED)
        Start(player).show()
    }

    override fun cli(commandSender: CommandSender, input: String) {

    }

    override fun response(player: Player, message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}