package interactions.menus

import Tickets.Companion.TicketManager
import interactions.InvPair
import interactions.Menu
import interactions.actions.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import storage.TicketSQL
import ticket.Ticket
import ticket.TicketStatus
import utility.Item

class Start(player: Player) : Menu(player, "Tickets", 18) {
    override fun load() {
        player().place(1)

        val tickets = TicketManager[player.uniqueId] ?: ArrayList()

        for (i in 3 until 8) {
            val ticket = tickets.getOrNull(i - 3)

            if (ticket == null) ticketPlaceholder().place(i)
            else ticket(ticket, i).place(i)
        }

        create(13).place(13)

        if (player.hasPermission("tickets.staff"))
            list().place(17)
    }

    private fun player(): InvPair {
        val item = Item(Material.PLAYER_HEAD).apply {
            name = "§f§l" + player.displayName

            TicketStatus.values().forEach {
                addLore("§7" + it.name.toLowerCase().capitalize() + ": §f" + TicketSQL.count(player.uniqueId, it))
            }

            addLore("")
            addLore("§7Left click to see your open tickets")
            addLore("§7Right click to see your closed tickets")

            setHead(player)
        }

        val runs = HashMap<ClickType, Runnable?>()

        runs[ClickType.LEFT] = Runnable {
            ViewTickets(player) {
                TicketManager[player.uniqueId]
            }.show()
        }

        runs[ClickType.RIGHT] = Runnable {
            ViewTickets(player) {
                TicketSQL.getInactive(player.uniqueId)
            }.show()
        }

        return InvPair(item, runs)
    }

    private fun create(location: Int): InvPair {
        val item = Item(Material.GREEN_STAINED_GLASS_PANE).apply {
            name = "§a§lCreate New Ticket"
        }

        val run = Runnable {
            tryInvoke(Create.gui(player), location)
        }

        return InvPair(item, run)
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

    private fun ticketPlaceholder(): InvPair {
        val item = Item(Material.FIREWORK_STAR).apply {
            name = " "
        }

        return InvPair(item)
    }

    private fun ticket(ticket: Ticket, location: Int): InvPair {
        val item = Item(Material.FILLED_MAP).apply {
            name = "§f§l" + ticket.currentMessage()
            addLore("§7Status: §a" + ticket.status.name)

            when (ticket.status) {
                TicketStatus.OPEN, TicketStatus.PICKED -> {
                    addLore("§7Left click to update the ticket")
                    addLore("§7Right click to close the ticket")
                }

                TicketStatus.CLOSED -> addLore("§7Left click to reopen the ticket")
            }

        }

        val runs = HashMap<ClickType, Runnable?>()

        runs[ClickType.LEFT] = Runnable {
            when (ticket.status) {
                TicketStatus.OPEN, TicketStatus.PICKED -> Update.tryAction(location, player, ticket)

                TicketStatus.CLOSED -> Reopen.tryAction(location, player, ticket)
            }
        }

        runs[ClickType.RIGHT] = Runnable {
            if (ticket.status != TicketStatus.CLOSED) {
                Close.tryAction(location, player, ticket)
            }
        }

        return InvPair(item, runs)
    }
}