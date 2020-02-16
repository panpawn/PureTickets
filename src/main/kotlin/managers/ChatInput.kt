package managers

import interactions.Action
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.collections.HashMap

class ChatInput : Listener {
    private val awaiting = HashMap<UUID, Action>()

    fun register(player: Player, action: Action) = awaiting.put(player.uniqueId, action)

    @EventHandler
    fun messageSend(e: AsyncPlayerChatEvent) {
        awaiting[e.player.uniqueId]?.also {
            e.isCancelled = true
            it.response(e.player, e.message)
        }

        awaiting.remove(e.player.uniqueId)
    }

    @EventHandler
    fun playerLeave(e: PlayerQuitEvent) = awaiting.remove(e.player.uniqueId)
}