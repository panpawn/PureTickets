package co.uk.magmo.puretickets.locale

import co.aikar.locales.MessageKey
import co.aikar.locales.MessageKeyProvider

enum class Messages : MessageKeyProvider {
    TASKS__REMINDER,
    TICKET__CREATED, TICKET__UPDATED, TICKET__CLOSED, TICKET__PICKED, TICKET__YIELDED, TICKET__DONE, TICKET__REOPENED,
    TICKET__TELEPORT, TICKET__TELEPORT_ERROR, TICKET__ASSIGN,
    NOTIFICATIONS__PICK, NOTIFICATIONS__YIELD, NOTIFICATIONS__DONE, NOTIFICATIONS__REOPEN, NOTIFICATIONS__ASSIGN,
    ANNOUNCEMENTS__NEW_TICKET, ANNOUNCEMENTS__UPDATED_TICKET, ANNOUNCEMENTS__CLOSED_TICKET, ANNOUNCEMENTS__PICKED_TICKET,
    ANNOUNCEMENTS__YIELDED_TICKET, ANNOUNCEMENTS__DONE_TICKET, ANNOUNCEMENTS__REOPEN_TICKET, ANNOUNCEMENTS__ASSIGN_TICKET,
    TITLES__SPECIFIC_TICKETS, TITLES__ALL_TICKETS, TITLES__SPECIFIC_STATUS, TITLES__TICKET_STATUS, TITLES__SHOW_TICKET,
    TITLES__TICKET_LOG,
    SHOW__SENDER, SHOW__PICKER, SHOW__UNPICKED, SHOW__MESSAGE,
    FORMAT__LIST_ITEM, FORMAT__SETTING_UPDATE;

    override fun getMessageKey(): MessageKey {
        return MessageKey.of(name.toLowerCase().replace("__", "."))
    }
}