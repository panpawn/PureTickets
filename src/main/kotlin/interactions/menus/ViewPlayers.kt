package interactions.menus

import Tickets.Companion.TicketManager
import interactions.InvPair
import interactions.Menu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import storage.TicketSQL
import ticket.TicketStatus
import utility.Item
import java.util.*

class ViewPlayers(player: Player) : Menu(player, "Open Tickets", 54) {
    override fun load() {
        val keys = TicketSQL.recentPlayers()

        for (i in keys.indices)
            player(keys[i]).place(i)
    }

    private fun player(uuid: UUID): InvPair {
        val target = Bukkit.getOfflinePlayer(uuid)

        val item = Item(Material.PLAYER_HEAD).apply {
            name = "§f§l" + target.name

            TicketStatus.values().forEach {
                addLore("§7" + it.name.toLowerCase().capitalize() + ": §f" + TicketSQL.count(target.uniqueId, it))
            }

            addLore("")
            addLore("§7Left click to see their open tickets")
            addLore("§7Right click to see their closed tickets")

            setHead(target)
        }

        val runs = HashMap<ClickType, Runnable?>()

        runs[ClickType.LEFT] = Runnable {
            ViewTickets(player) {
                TicketManager[target.uniqueId]
            }.show()
        }

        runs[ClickType.RIGHT] = Runnable {
            ViewTickets(player) {
                TicketSQL.getInactive(target.uniqueId)
            }.show()
        }

        return InvPair(item, runs)
    }
}