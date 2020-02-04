import interactions.Dispatcher
import interactions.TabCompletion
import managers.ChatInput
import managers.InventoryManager
import managers.Notifications
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import storage.SQL
import ticket.TicketManager

class Tickets : JavaPlugin() {
    override fun onEnable() {
        dataFolder.mkdir()

        TICKETS = this
        SQLManager = SQL()
        TicketManager = TicketManager()
        InventoryManager = InventoryManager()
        ChatInput = ChatInput()

        val base = getCommand("ticket")
        base?.aliases = listOf("ti")
        base?.setExecutor(Dispatcher())
        base?.tabCompleter = TabCompletion

        server.pluginManager.registerEvents(InventoryManager, this)
        server.pluginManager.registerEvents(ChatInput, this)
        server.pluginManager.registerEvents(Notifications, this)
    }

    override fun onDisable() {
        SQLManager.close()
    }

    companion object {
        lateinit var TICKETS: Plugin

        lateinit var SQLManager: SQL
        lateinit var TicketManager: TicketManager
        lateinit var InventoryManager: InventoryManager
        lateinit var ChatInput: ChatInput
    }
}