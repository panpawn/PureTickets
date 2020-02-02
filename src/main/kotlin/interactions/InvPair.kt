package interactions

import org.bukkit.event.inventory.ClickType
import utility.Item

class InvPair {
    val icon: Item
    val run: HashMap<ClickType, Runnable?>

    constructor(icon: Item, run: Runnable? = null) {
        this.icon = icon
        val map = HashMap<ClickType, Runnable?>()
        map[ClickType.LEFT] = run
        this.run = map
    }

    constructor(icon: Item, runs: HashMap<ClickType, Runnable?>) {
        this.icon = icon
        this.run = runs
    }
}
