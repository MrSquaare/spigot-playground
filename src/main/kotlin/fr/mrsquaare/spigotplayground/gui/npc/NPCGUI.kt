package fr.mrsquaare.spigotplayground.gui.npc

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.gui.BaseInventoryGUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class NPCGUI(
    plugin: SpigotPlaygroundPlugin,
    private val player: Player,
    private val onRename: (() -> Unit),
    private val onChangeSkin: (() -> Unit),
    private val onDelete: (() -> Unit)
) : BaseInventoryGUI<Inventory>(plugin) {
    override val items = listOf(
        object : BaseGUIItem(plugin) {
            override val slot = 3
            override val stack = object : Stack(
                plugin = plugin,
                name = "npc_gui_rename",
                displayName = "Rename NPC",
                material = Material.NAME_TAG
            ) {}

            override fun onClick(event: InventoryClickEvent) {
                event.whoClicked.closeInventory()

                onRename()
            }
        },
        object : BaseGUIItem(plugin) {
            override val slot = 4
            override val stack = object : Stack(
                plugin = plugin,
                name = "npc_gui_change_skin",
                displayName = "Change NPC Skin",
                material = Material.PLAYER_HEAD
            ) {}

            override fun onClick(event: InventoryClickEvent) {
                event.whoClicked.closeInventory()

                onChangeSkin()
            }
        },
        object : BaseGUIItem(plugin) {
            override val slot = 5
            override val stack = object : Stack(
                plugin = plugin,
                name = "npc_gui_delete",
                displayName = "Delete NPC",
                material = Material.BARRIER
            ) {}

            override fun onClick(event: InventoryClickEvent) {
                event.whoClicked.closeInventory()

                onDelete()
            }
        }
    )

    override var inventory: Inventory? = null

    public override fun open() {
        super.open()

        inventory = plugin.server.createInventory(player, 9, "NPC")

        items.forEach { item ->
            inventory?.setItem(item.slot, item.stack)
        }

        player.openInventory(inventory!!)
    }

    override fun onClick(event: InventoryClickEvent) {
        event.isCancelled = true
    }

    override fun onClose(event: InventoryCloseEvent) {
        event.inventory.clear()
    }
}
