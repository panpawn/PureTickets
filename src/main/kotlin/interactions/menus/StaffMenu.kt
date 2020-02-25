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

        list().place(3)

        recent().place(5)
    }

    private fun personal(): InvPair {
        val pickedBy = TicketManager.all().filter { it.status == TicketStatus.PICKED }.filter { it.picker == player.uniqueId }

        val item = Item(Material.PLAYER_HEAD).apply {
            setHead(player)
            addLore("§7Amount picked by you: " + pickedBy.size)

            addLore("")
            addLore("§7Click to view")
        }

        val runnable = Runnable {
            ViewTickets(player) {
                pickedBy
            }.show()
        }

        return InvPair(item, runnable)
    }

    private fun list(): InvPair {
        val item = Item(Material.BOOK).apply {
            name = "§f§lView Tickets"
            addLore("§7Tickets in: " + TicketManager.countNot(TicketStatus.CLOSED))

            addLore("")
            addLore("§7Left click to see recent ticket creators")
        }

        val run = Runnable {
            ViewPlayers(player).show()
        }

        return InvPair(item, run)
    }

    private fun recent(): InvPair {
        val item = Item(Material.CLOCK).apply {
            name = "§f§lView New Tickets"

            addLore("")
            addLore("§7Left click to see new tickets")
        }

        val runnable = Runnable {
            ViewTickets(player) {
                TicketManager.all().filter { it.status == TicketStatus.OPEN }.sortedBy { it.messages.last().date }
            }.show()
        }

        return InvPair(item, runnable)
    }
}
