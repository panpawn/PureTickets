package interactions.menus

import Tickets.Companion.TicketManager
import interactions.InvPair
import interactions.Menu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import storage.TicketSQL
import ticket.Ticket
import ticket.TicketStatus
import utility.Item

class PersonalTickets(player: Player, private val active: Boolean) : Menu(player, "§l" + player.name + "'s Tickets", if (active) 18 else 36) {
    override fun load() {
        val tickets = if (active) TicketManager[player.uniqueId] ?: ArrayList()
        else TicketSQL.getInactive(player.uniqueId)

        for (i in 0 until tickets.size) ticket(tickets[i]).place(i)
    }

    private fun ticket(ticket: Ticket): InvPair {
        val item = Item(Material.FILLED_MAP).apply {
            name = "§f§l" + ticket.currentMessage()
            addLore("§7Status: §a" + ticket.status.name)

            if (ticket.status == TicketStatus.PICKED) {
                glow()
                addLore("§7Picked by: §a" + Bukkit.getOfflinePlayer(ticket.uuid).name)
            }
        }

        val runnable = Runnable {
            ticket.setStatus(TicketStatus.CLOSED)
            show()
        }

        return InvPair(item, runnable)
    }
}