package interactions

import interactions.actions.Action
import interactions.actions.Create
import interactions.actions.Pick
import interactions.actions.Update

enum class Actions(val action: Action, val isAlias: Boolean = false) {
    CREATE(Create), C(Create, true),
    UPDATE(Update), U(Update, true),
    PICK(Pick), P(Pick, true);

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