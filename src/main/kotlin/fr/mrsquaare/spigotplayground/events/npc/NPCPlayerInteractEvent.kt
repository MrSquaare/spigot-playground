package fr.mrsquaare.spigotplayground.events.npc

import fr.mrsquaare.spigotplayground.models.NPC
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class NPCPlayerInteractEvent(val npc: NPC, val player: Player, val item: ItemStack) : Event() {
    companion object {
        @JvmStatic
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }
}
