package interactions.menus

import Tickets.Companion.TicketManager
import interactions.InvPair
import interactions.Menu
import org.bukkit.Material
import org.bukkit.entity.Player
import ticket.TicketStatus
import utility.Item

class StaffMenu(player: Player) : Menu(player, "Manage PureTickets", 18) {
    override fun load() {
        personal().place(1)

        players().place(3)

        recent().place(5)
    }

    private fun personal(): InvPair {
        val pickedBy = TicketManager.all().filter { it.status == TicketStatus.PICKED }.filter { it.picker == player.uniqueId }

        val item = Item(Material.PLAYER_HEAD).apply {
            name = "§f§lYour Tickets"

            addLore("§7Picked by you: " + pickedBy.size)

            setHead(player)
        }

        val runnable = Runnable {
            ViewTickets(player) {
                pickedBy
            }.show()
        }

        return InvPair(item, runnable)
    }

    private fun players(): InvPair {
        val item = Item(Material.BOOK).apply {
            name = "§f§lView Tickets"
            addLore("§7Tickets in: " + TicketManager.countNot(TicketStatus.CLOSED))
        }

        val run = Runnable {
            ViewPlayers(player).show()
        }

        return InvPair(item, run)
    }

    private fun recent(): InvPair {
        val item = Item(Material.CLOCK).apply {
            name = "§f§lView New Tickets"
        }

        val runnable = Runnable {
            ViewTickets(player) {
                TicketManager.all().filter { it.status == TicketStatus.OPEN }.sortedBy { it.messages.last().date }
            }.show()
        }

        return InvPair(item, runnable)
    }
}
