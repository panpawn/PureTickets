package managers

import Tickets
import utility.log

class Config(TICKETS: Tickets) {
    private val configInterval = TICKETS.config.get("ReminderInterval")

    val reminderInterval =
        if (configInterval is Int) configInterval else 10
            .also { log("ReminderInterval is not defined correctly, it has been set to 10") }

    private val configLocale = TICKETS.config.get("Locale")

    val locale =
        if (configLocale is String) configLocale else "en"
            .also { log("Locale is not defined correctly, it has been set to en") }
}