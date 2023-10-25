package fr.mrsquaare.spigotplayground.gui

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.items.BaseItem
import fr.mrsquaare.spigotplayground.items.ListenableItem
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory

abstract class BaseInventoryGUI<I : Inventory>(
    protected val plugin: SpigotPlaygroundPlugin,
) {
    protected abstract val items: List<BaseGUIItem>

    abstract class BaseGUIItem(plugin: SpigotPlaygroundPlugin) : BaseItem(plugin) {
        abstract val slot: Int

        open fun onClick(event: InventoryClickEvent) {}
    }

    protected abstract var inventory: I?

    protected open fun open() {
        plugin.server.pluginManager.registerEvents(listen, plugin)

        items.forEach {
            if (it is ListenableItem) {
                plugin.server.pluginManager.registerEvents(it.listen, plugin)
            }
        }
    }

    open fun onOpen(event: InventoryOpenEvent) {}
    open fun onClick(event: InventoryClickEvent) {}
    open fun onClose(event: InventoryCloseEvent) {}

    open val listen = Listen()

    open inner class Listen : Listener {
        @EventHandler
        private fun onInventoryOpen(event: InventoryOpenEvent) {
            if (event.inventory != inventory) return

            onOpen(event)
        }

        @EventHandler
        private fun onInventoryClick(event: InventoryClickEvent) {
            if (event.inventory != inventory) return

            val item = items.find { it.slot == event.slot }

            onClick(event)

            item?.onClick(event)
        }

        @EventHandler
        private fun onInventoryClose(event: InventoryCloseEvent) {
            if (event.inventory != inventory) return

            onClose(event)

            HandlerList.unregisterAll(listen)
        }
    }
}
