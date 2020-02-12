package interactions.menus

import Tickets.Companion.TicketManager
import interactions.InvPair
import interactions.Menu
import interactions.actions.Done
import interactions.actions.Pick
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import storage.TicketSQL
import ticket.Ticket
import ticket.TicketStatus
import utility.Item
import utility.staff

class IndividualsTickets(player: Player, private val target: OfflinePlayer, private val active: Boolean) : Menu(player, "§l" + target.name + "'s Tickets", if (active) 18 else 36) {
    override fun load() {
        val tickets = if (active) TicketManager[target.uniqueId] ?: ArrayList()
        else TicketSQL.getInactive(target.uniqueId)

        for (i in 0 until tickets.size) ticket(tickets[i], i).place(i)
    }

    private fun ticket(ticket: Ticket, location: Int): InvPair {
        val item = Item(Material.FILLED_MAP).apply {
            name = "§f§l" + ticket.currentMessage()
            addLore("§7Status: §a" + ticket.status.name)

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

        val runs = HashMap<ClickType, Runnable?>()

        runs[ClickType.LEFT] = Runnable {
            when (ticket.status) {
                TicketStatus.OPEN -> {
                    Pick.gui(player, ticket)
                    show()
                }

                TicketStatus.PICKED -> {
                    if (ticket.picker == player.uniqueId) Done.gui(player, ticket)
                    else flashDeny(location, "You cannot done a ticket", "that is picked by someone else")
                }

                TicketStatus.CLOSED -> {
                    ticket.setStatus(TicketStatus.PICKED, player)
                    ticket.notify(player.name + " has reopened and picked your ticket")
                    TicketManager.add(target.uniqueId, ticket)
                    show()
                }
            }
        }

        runs[ClickType.RIGHT] = Runnable {
            if (ticket.status == TicketStatus.PICKED && ticket.picker == player.uniqueId) {
                ticket.setStatus(TicketStatus.OPEN, null)
                ticket.notify(player.name + " has yielded your ticket")
                show()
            }
        }

        return InvPair(item, runs)
    }
}