package fr.mrsquaare.spigotplayground.items

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

abstract class BaseItem(protected val plugin: SpigotPlaygroundPlugin) {
    abstract val stack: Stack

    abstract class Stack(
        protected val plugin: SpigotPlaygroundPlugin,
        name: String,
        displayName: String,
        material: Material,
    ) : ItemStack(material) {
        val namespacedKey: NamespacedKey = NamespacedKey(plugin, name)

        init {
            val meta = itemMeta
            val persistentDataContainer = meta?.persistentDataContainer

            meta?.setDisplayName(displayName)
            persistentDataContainer?.set(namespacedKey, PersistentDataType.BYTE, 1)

            itemMeta = meta
        }

        fun isItem(itemStack: ItemStack?): Boolean {
            return itemStack?.type == type && itemStack.itemMeta?.persistentDataContainer?.has(
                namespacedKey,
                PersistentDataType.BYTE
            ) ?: false
        }
    }
}
