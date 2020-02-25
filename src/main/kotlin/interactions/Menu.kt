package interactions

import Tickets.Companion.InventoryManager
import interactions.menus.PlayerMenu
import interactions.menus.StaffMenu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import utility.Item

abstract class Menu(val player: Player, title: String, size: Int) {
    private var runs = HashMap<Int, HashMap<ClickType, Runnable?>>()
    private var inv = Bukkit.createInventory(null, size, title)

    abstract fun load()

    open fun show() {
        inv.clear()
        load()

        val icon = page(player)
        for (i in inv.size - 9 until inv.size)
            if (inv.getItem(i)?.type == null)
                icon.place(i)

        player.openInventory(inv)
        InventoryManager[player] = this
    }

    fun InvPair.place(i: Int) {
        inv.setItem(i, icon.get())
        runs[i] = run
    }

    fun click(e: InventoryClickEvent) {
        e.isCancelled = true

        runs[e.slot]?.get(e.click)?.run()
    }

    companion object {
        fun page(player: Player): InvPair {
            val item = Item(Material.PURPLE_STAINED_GLASS_PANE).apply {
                name = "§f§lGo Back"
                addLore("§7You can click this to")
                addLore("§7go back to the Main Screen")

                if (player.hasPermission("tickets.staff.menu")) {
                    addLore("")
                    addLore("§7Right click to view Admin HUD")
                }
            }

            val runs = HashMap<ClickType, Runnable?>()

            runs[ClickType.LEFT] = Runnable { PlayerMenu(player).show() }

            runs[ClickType.RIGHT] = Runnable {
                if (player.hasPermission("tickets.staff.menu"))
                    StaffMenu(player).show()
            }

            return InvPair(item, runs)
        }
    }
}