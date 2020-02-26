package interactions

import interactions.actions.*
import interactions.actions.List

enum class Actions(val action: Action, val isAlias: Boolean = false) {
    CREATE(Create), C(Create, true),
    CLOSE(Close), CL(Close, true),
    DONE(Done), D(Done, true),
    LIST(List), L(List, true),
    REOPEN(Reopen), R(Reopen, true),
    UPDATE(Update), U(Update, true),
    PICK(Pick), P(Pick, true),
    YIELD(Yield), Y(Yield, true);

    companion object {
        fun find(input: String): Actions? {
            val search = input.toUpperCase()

            for (action in values()) {
                if (action.name == search)
                    return action
            }

            return null
        }
    }
}