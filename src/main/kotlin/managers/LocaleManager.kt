package managers

import Tickets
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.*

class LocaleManager(private val TICKETS: Tickets, config: Config) {
    private val folder = File(TICKETS.dataFolder, "locale/")
    private var prop = Properties()

    init {
        if (!folder.exists()) folder.mkdir()
        copyFileFromJar("en")
        copyFileFromJar("se")
        copyFileFromJar("es")

        loadFile(config.locale)
    }

    fun retrieve(key: String) = prop.getOrDefault(key, "No Locale!") as String

    inner class Locale {
        val noPermission = retrieve("NO_PERMISSION")
        val commandDoesntExist = retrieve("COMMAND_DOESNT_EXIST")

        val player = retrieve("PLAYER")
        val message = retrieve("MESSAGE")
        val coords = retrieve("COORDS")
        val timePast = retrieve("TIME_PAST")
        val staff = retrieve("STAFF")

        val created = retrieve("CREATED")
        val updated = retrieve("UPDATED")
        val notOpen = retrieve("NOT_OPEN")
        val ticketNotFound = retrieve("TICKET_NOT_FOUND")
        val playerNotFound = retrieve("PLAYER_NOT_FOUND")

        val notPicked = retrieve("NOT_PICKED")
        val alreadyPicked = retrieve("ALREADY_PICKED")

        val playerDone = retrieve("PLAYER_DONE")
        val playerSelfDone = retrieve("PLAYER_SELF_DONE")

        val playerBlocked = retrieve("PLAYER_BLOCKED")
        val teleported = retrieve("TELEPORTED")

        private val amtAvailable = retrieve("AMT_AVAILABLE")
        fun amtAvailable(amt: Int) = amtAvailable.replace("%a", amt.toString())

        private val playerPicked = retrieve("PLAYER_PICKED")
        fun playerPicked(s: String, c: String) = playerPicked.staff(s, c)

        private val staffPicked = retrieve("STAFF_PICKED")
        fun staffPicked(p: String, s: String, c: String) = staffPicked.player(p, c).staff(s, c)

        private val playerStaffDone = retrieve("PLAYER_STAFF_DONE")
        fun playerStaffDone(s: String, c: String) = playerStaffDone.staff(s, c)

        private val staffDone = retrieve("STAFF_DONE")
        fun staffDone(p: String, c: String) = staffDone.player(p, c)

        private val staffPlayerDone = retrieve("STAFF_PLAYER_DONE")
        fun staffPlayerDone(p: String, c: String) = staffPlayerDone.player(p, c)

        private val staffSelfDone = retrieve("STAFF_SELF_DONE")
        fun staffSelfDone(p: String, s: String, c: String) = staffSelfDone.player(p, c).staff(s, c)

        private val playerYielded = retrieve("PLAYER_YIELDED")
        fun playerYielded(s: String, c: String) = playerYielded.staff(s, c)

        private val staffYielded = retrieve("STAFF_YIELDED")
        fun staffYielded(p: String, s: String, c: String) = staffYielded.player(p, c).staff(s, c)

        private val removedBlock = retrieve("REMOVED_BLOCK")
        fun removedBlock(p: String, c: String) = removedBlock.player(p, c)

        private val addedBlock = retrieve("ADDED_BLOCK")
        fun addedBlock(p: String, c: String) = addedBlock.player(p, c)

        private fun String.player(p: String, c: String) = this.replace("%p", "§f§l$p$c")
        private fun String.staff(s: String, c: String) = this.replace("%s", "§f§l$s$c")
    }

    private fun loadFile(input: String) {
        val name = "locale_${input}.properties"
        val target = File(folder, name).inputStream()
        prop.load(InputStreamReader(target, Charset.forName("UTF-8")))
        target.close()
    }

    private fun copyFileFromJar(input: String) {
        val name = "locale_${input}.properties"
        val target = File(folder, name)

        if (!target.exists()) {
            val stream = TICKETS.javaClass.getResourceAsStream("/locale/$name")
            Files.copy(stream, target.absoluteFile.toPath())
        }
    }
}