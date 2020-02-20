package managers

import Tickets.Companion.TICKETS
import interactions.Menu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.scheduler.BukkitTask
import utility.Item
import utility.splitToAdd
import java.util.*
import kotlin.collections.HashMap

class InventoryManager : Listener {
    private val currentMenu = HashMap<UUID, Menu>()
    private val pendingTasks = HashMap<UUID, BukkitTask>()

    @EventHandler
    fun click(e: InventoryClickEvent) = currentMenu[e.whoClicked.uniqueId]?.click(e)

    @EventHandler(priority = EventPriority.LOWEST)
    fun close(e: InventoryCloseEvent) {
        currentMenu.remove(e.player.uniqueId)
        pendingTasks.remove(e.player.uniqueId)
    }

    operator fun set(player: Player, menu: Menu) {
        currentMenu[player.uniqueId] = menu
    }

    operator fun set(player: Player, task: BukkitTask) {
        pendingTasks[player.uniqueId] = task
    }

    fun displayError(location: Int, player: Player, error: String?) {
        player.openInventory.setItem(location, Item(Material.BARRIER).apply {
            name = "§4§lDENIED"
            error?.splitToAdd()?.forEach { addLore(it) }
        }.get())

        pendingTasks[player.uniqueId] = Bukkit.getScheduler().runTaskLater(TICKETS, Runnable {
            currentMenu[player.uniqueId]?.show()
        }, 120)
    }

    fun refresh(player: Player) {
        currentMenu[player.uniqueId]?.show()
    }
}