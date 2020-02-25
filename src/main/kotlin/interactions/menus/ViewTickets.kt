package interactions.menus

import interactions.InvPair
import interactions.Menu
import interactions.actions.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import ticket.Ticket
import ticket.TicketStatus
import utility.Item

class ViewTickets(player: Player, private val tickets: () -> List<Ticket>?) : Menu(player, "View Tickets", 45) {
    override fun load() {
        val ticketInstance = tickets.invoke() ?: return

        for (i in ticketInstance.indices) {
            if (i + 1 > 36) break

            ticket(ticketInstance[i], i).place(i)
        }
    }

    private fun ticket(ticket: Ticket, location: Int): InvPair {
        val item = Item(Material.FILLED_MAP).apply {
            name = "§f§l" + ticket.currentMessage()
            addLore("§7Status: §a" + ticket.status.name)

            if (player.uniqueId == ticket.uuid) {
                when (ticket.status) {
                    TicketStatus.OPEN, TicketStatus.PICKED -> {
                        addLore("§7Left click to update the ticket")
                        addLore("§7Right click to close the ticket")
                    }

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
            if (player.uniqueId == ticket.uuid) {
                when (ticket.status) {
                    TicketStatus.OPEN, TicketStatus.PICKED -> Update.tryAction(location, player, ticket)

                    TicketStatus.CLOSED -> Reopen.tryAction(location, player, ticket)
                }
            } else {
                when (ticket.status) {
                    TicketStatus.OPEN -> Pick.tryAction(location, player, ticket)

                    TicketStatus.PICKED -> Done.tryAction(location, player, ticket)

                    TicketStatus.CLOSED -> Reopen.tryAction(location, player, ticket)
                }
            }
        }

        runs[ClickType.RIGHT] = Runnable {
            if (player.uniqueId == ticket.uuid && ticket.status != TicketStatus.CLOSED) {
                Close.tryAction(location, player, ticket)
            } else if (ticket.status == TicketStatus.PICKED && ticket.picker == player.uniqueId) {
                Yield.tryAction(location, player, ticket)
            }
        }

        return InvPair(item, runs)
    }
}