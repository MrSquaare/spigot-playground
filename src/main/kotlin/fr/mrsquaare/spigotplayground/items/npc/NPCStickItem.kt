package fr.mrsquaare.spigotplayground.items.npc

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.events.hub.HubPlayerJoinEvent
import fr.mrsquaare.spigotplayground.events.npc.NPCPlayerInteractEvent
import fr.mrsquaare.spigotplayground.gui.InputGUI
import fr.mrsquaare.spigotplayground.gui.npc.NPCGUI
import fr.mrsquaare.spigotplayground.items.BaseItem
import fr.mrsquaare.spigotplayground.items.ListenableItem
import fr.mrsquaare.spigotplayground.models.NPC
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class NPCStickItem(plugin: SpigotPlaygroundPlugin) : BaseItem(plugin), ListenableItem {
    override val stack = Stack(plugin);

    class Stack(plugin: SpigotPlaygroundPlugin) : BaseItem.Stack(
        plugin = plugin,
        name = "npc_stick",
        displayName = "NPC Stick",
        material = Material.STICK
    )

    override val listen = Listen()

    inner class Listen : Listener {
        @EventHandler
        fun onPlayerJoinHub(event: HubPlayerJoinEvent) {
            val player = event.player

            player.inventory.addItem(stack)
        }

        @EventHandler
        fun onPlayerInteract(event: PlayerInteractEvent) {
            val item = event.item ?: return

            if (!stack.isItem(item)) return

            event.isCancelled = true

            if (event.action === Action.RIGHT_CLICK_BLOCK) {
                handleCreateNPC(event)
            }
        }

        @EventHandler
        fun onNPCPlayerInteract(event: NPCPlayerInteractEvent) {
            val item = event.item

            if (!stack.isItem(item)) return

            NPCGUI(
                plugin = plugin,
                player = event.player,
                onEdit = {
                    handleEditNPC(event)
                },
                onDelete = {
                    handleDeleteNPC(event)
                }
            ).open()
        }

        private fun handleCreateNPC(event: PlayerInteractEvent) {
            val block = event.clickedBlock ?: return
            val location = block.location.clone()

            location.y += 1.0

            InputGUI(
                plugin = plugin,
                player = event.player,
                title = "Enter NPC name",
                onInput = { name ->
                    if (!checkInput(name, event.player)) return@InputGUI

                    val npc = NPC(name, event.player.server, event.player.world, location)

                    plugin.npcManager.addNPC(npc)

                    npc.spawn()

                    val spigotPlayer = event.player.spigot()

                    spigotPlayer.sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent("§aThe NPC has been created.")
                    )
                    event.player.playSound(event.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                },
            ).open()
        }

        private fun handleEditNPC(event: NPCPlayerInteractEvent) {
            InputGUI(
                plugin = plugin,
                player = event.player,
                title = "Enter NPC name",
                defaultValue = event.npc.entity.displayName,
                onInput = { name ->
                    if (!checkInput(name, event.player)) return@InputGUI

                    event.npc.rename(name)

                    val spigotPlayer = event.player.spigot()

                    spigotPlayer.sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent("§aThe NPC has been renamed.")
                    )
                    event.player.playSound(event.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                },
            ).open()
        }

        private fun handleDeleteNPC(event: NPCPlayerInteractEvent) {
            event.npc.despawn()

            plugin.npcManager.removeNPC(event.npc.entity.id)

            val spigotPlayer = event.player.spigot()

            spigotPlayer.sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent("§aThe NPC has been deleted.")
            )
            event.player.playSound(event.player.location, Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f)
        }

        private fun checkInput(input: String, player: Player): Boolean {
            if (input.length <= 16) return true

            val spigotPlayer = player.spigot()

            spigotPlayer.sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent("§cThe name must be less than 16 characters.")
            )
            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)

            return false
        }
    }
}
