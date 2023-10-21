package fr.mrsquaare.spigotplayground.gui

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.world.inventory.AnvilMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R2.event.CraftEventFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.AnvilInventory

class InputGUI(
    plugin: SpigotPlaygroundPlugin,
    private val player: Player,
    private val title: String,
    private val defaultValue: String? = null,
    private val onInput: ((String) -> Unit)? = null,
    private val onClick: ((InventoryClickEvent) -> Unit)? = null,
    private val onClose: ((InventoryCloseEvent) -> Unit)? = null
) : BaseInventoryGUI<AnvilInventory>(plugin) {
    override val items = listOf<BaseGUIItem>(
        object : BaseGUIItem(plugin) {
            override val slot = 0
            override val stack = object : Stack(
                plugin = plugin,
                name = "input_gui_item",
                displayName = defaultValue ?: "${ChatColor.WHITE}",
                material = Material.NAME_TAG
            ) {}
        }
    )

    override var inventory: AnvilInventory? = null

    public override fun open() {
        super.open()

        val nmsPlayer = (player as CraftPlayer).handle

        CraftEventFactory.handleInventoryCloseEvent(nmsPlayer)

        nmsPlayer.containerMenu = nmsPlayer.inventoryMenu

        val containerId = nmsPlayer.nextContainerCounter()

        val nmsLevel = nmsPlayer.level()

        val nmsAnvilMenu = AnvilMenu(
            containerId, nmsPlayer.inventory, ContainerLevelAccess.create(nmsLevel, BlockPos(0, 0, 0))
        )
        nmsAnvilMenu.checkReachable = false
        nmsAnvilMenu.title = Component.literal(title)

        inventory = nmsAnvilMenu.bukkitView?.topInventory as AnvilInventory?

        items.forEach { item ->
            inventory?.setItem(item.slot, item.stack)
        }

        nmsPlayer.connection.send(
            ClientboundOpenScreenPacket(
                nmsAnvilMenu.containerId, nmsAnvilMenu.type, nmsAnvilMenu.title
            )
        )
        nmsPlayer.containerMenu = nmsAnvilMenu
        nmsPlayer.initMenu(nmsAnvilMenu)
    }

    override fun onClick(event: InventoryClickEvent) {
        onClick?.invoke(event)

        event.isCancelled = true

        val lambda = onInput ?: return

        val newName = (inventory as AnvilInventory).renameText ?: return

        if (newName.isBlank()) return

        lambda.invoke(newName)

        event.whoClicked.closeInventory()
    }

    override fun onClose(event: InventoryCloseEvent) {
        onClose?.invoke(event)

        event.inventory.clear()
    }
}
