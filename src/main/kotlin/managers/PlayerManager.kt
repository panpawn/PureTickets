package managers

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import storage.TicketSQL
import java.util.*

class PlayerManager : Listener {
    private val playerProfiles = HashMap<UUID, PlayerProfile>()

    operator fun get(uuid: UUID) = playerProfiles[uuid]

    @EventHandler
    fun join(e: PlayerJoinEvent) {
        val uuid = e.player.uniqueId

        if (!playerProfiles.containsKey(uuid)) {
            if (TicketSQL.playerProfileExists(uuid)) {
                playerProfiles[uuid] = TicketSQL.getPlayerProfile(e.player.uniqueId)
            } else {
                val playerProfile = PlayerProfile(uuid, 0, false)

                playerProfiles[uuid] = playerProfile
                TicketSQL.insPlayerProfile(playerProfile)
            }
        }
    }

    class PlayerProfile(val uuid: UUID, private var points: Int, private var blocked: Boolean) {
        fun getPoints() = points

        fun isBlocked() = blocked

        fun incrementPoints() {
            points++
            TicketSQL.updPlayerProfile(this)
        }

        fun setBlocked(newValue: Boolean) {
            blocked = newValue
            TicketSQL.updPlayerProfile(this)
        }
    }
}