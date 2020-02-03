package managers

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import utility.message
import java.util.*
import kotlin.collections.HashMap

object Notifications : Listener {
    private val awaiting = HashMap<UUID, ArrayList<String>>()

    fun send(uuid: UUID, message: String) {
        val op = Bukkit.getOfflinePlayer(uuid)

        if (op.isOnline) {
            val p = op as Player
            p.message(message)
        } else {
            awaiting.putIfAbsent(op.uniqueId, ArrayList())
            awaiting[op.uniqueId]?.add(message)
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        awaiting[e.player.uniqueId]?.forEach {
            e.player.message(it)
        }
    }
}