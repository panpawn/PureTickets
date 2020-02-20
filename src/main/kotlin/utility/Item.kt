package utility

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class Item(input: ItemStack?) {
    private var item = input!!
    private var meta = item.itemMeta!!
    private var type = item.type

    constructor(input: Material) : this(ItemStack(input))

    var lore = (meta.lore ?: ArrayList()) as ArrayList<String>
        set(value) {
            field = value
            meta.lore = field
        }

    var name = meta.displayName
        set(value) {
            field = value
            meta.setDisplayName(field)
        }

    fun addLore(input: String) {
        val newLore = lore
        newLore.add(input)
        lore = newLore
    }

    fun setHead(player: OfflinePlayer) {
        val skull = meta as SkullMeta
        skull.owningPlayer = player

        meta = skull
    }

    fun glow() {
        val newMeta = meta
        newMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        newMeta.addEnchant(Enchantment.DURABILITY, 1, false)

        meta = newMeta
    }

    fun get(): ItemStack {
        item.itemMeta = meta
        return item
    }
}