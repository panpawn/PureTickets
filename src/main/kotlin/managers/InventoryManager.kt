package managers

import interactions.Menu
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.scheduler.BukkitTask
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
}