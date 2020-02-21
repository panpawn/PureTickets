import interactions.TicketCommand
import managers.ChatInput
import managers.InventoryManager
import managers.Notifications
import managers.PlayerManager
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import storage.SQLManager
import ticket.TicketManager

class Tickets : JavaPlugin() {
    override fun onEnable() {
        dataFolder.mkdir()

        TICKETS = this
        SQLManager = SQLManager()
        PlayerManager = PlayerManager()
        TicketManager = TicketManager()
        InventoryManager = InventoryManager()
        ChatInput = ChatInput()

        val base = getCommand("ticket")
        base?.aliases = listOf("ti")
        base?.setExecutor(TicketCommand)
        base?.tabCompleter = TicketCommand

        server.pluginManager.registerEvents(InventoryManager, this)
        server.pluginManager.registerEvents(ChatInput, this)
        server.pluginManager.registerEvents(Notifications, this)
    }

    override fun onDisable() {
        SQLManager.close()
    }

    companion object {
        lateinit var TICKETS: Plugin

        lateinit var SQLManager: SQLManager
        lateinit var PlayerManager: PlayerManager
        lateinit var TicketManager: TicketManager
        lateinit var InventoryManager: InventoryManager
        lateinit var ChatInput: ChatInput
    }
}