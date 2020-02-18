package interactions.menus

import interactions.InvPair
import interactions.Menu
import interactions.actions.Done
import interactions.actions.Pick
import interactions.actions.Reopen
import interactions.actions.Yield
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import ticket.Ticket
import ticket.TicketStatus
import utility.Item

class ViewTickets(player: Player, private val tickets: () -> ArrayList<Ticket>?) : Menu(player, "View Tickets", 45) {
    override fun load() {
        val ticketInstance = tickets.invoke() ?: return

        for (i in 0 .. ticketInstance.size) {
            ticket(ticketInstance[i], i).place(i)
        }
    }

    fun ticket(ticket: Ticket, location: Int): InvPair {
        val item = Item(Material.FILLED_MAP).apply {
            name = "§f§l" + ticket.currentMessage()
            addLore("§7Status: §a" + ticket.status.name)

            if (player.uniqueId == ticket.uuid) {
                when (ticket.status) {
                    TicketStatus.OPEN, TicketStatus.PICKED -> addLore("§7Left click to pick the ticket")

                    TicketStatus.CLOSED -> addLore("§7Left click to reopen the ticket")
                }
            } else {
                when (ticket.status) {
                    TicketStatus.OPEN -> addLore("§7Left click to pick the ticket")

                    TicketStatus.PICKED -> {
                        glow()
                        addLore("§7Picked by: §a" + Bukkit.getOfflinePlayer(ticket.picker!!).name)

                        if (ticket.picker == player.uniqueId) {
                            addLore("§7Left click to done-mark the ticket")
                            addLore("§7Right click to yield the ticket")
                        }
                    }

                    TicketStatus.CLOSED -> addLore("§7Left click to reopen the ticket")
                }
            }
        }

        val runs = HashMap<ClickType, Runnable?>()

        runs[ClickType.LEFT] = Runnable {
            when (ticket.status) {
                TicketStatus.OPEN -> tryInvoke(Pick.gui(player, ticket), location)

                TicketStatus.PICKED -> tryInvoke(Done.gui(player, ticket), location)

                TicketStatus.CLOSED -> tryInvoke(Reopen.gui(player, ticket), location)
            }
        }

        runs[ClickType.RIGHT] = Runnable {
            if (ticket.status == TicketStatus.PICKED && ticket.picker == player.uniqueId) {
                Yield.gui(player, ticket)
            }
        }

        return InvPair(item, runs)
    }
}