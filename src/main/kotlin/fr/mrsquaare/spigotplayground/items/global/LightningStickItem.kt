package fr.mrsquaare.spigotplayground.items.global

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.items.BaseItem
import fr.mrsquaare.spigotplayground.items.CraftableItem
import fr.mrsquaare.spigotplayground.items.ListenableItem
import org.bukkit.FluidCollisionMode
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ShapedRecipe

class LightningStickItem(plugin: SpigotPlaygroundPlugin) : BaseItem(plugin), CraftableItem, ListenableItem {
    override val stack = Stack(plugin);

    class Stack(plugin: SpigotPlaygroundPlugin) : BaseItem.Stack(
        plugin = plugin,
        name = "lightning_stick",
        displayName = "Lightning Stick",
        material = Material.STICK
    )

    override val craft = Craft()

    inner class Craft : ShapedRecipe(stack.namespacedKey, stack) {
        init {
            shape(
                "  B",
                " A ",
                "A  "
            )

            setIngredient('A', Material.STICK)
            setIngredient('B', Material.LIGHTNING_ROD)
        }
    }

    override val listen = Listen()

    inner class Listen : Listener {
        @EventHandler
        fun onPlayerInteract(event: PlayerInteractEvent) {
            val player = event.player
            val item = player.inventory.itemInMainHand

            if (!stack.isItem(item)) return

            event.isCancelled = true

            val maxDistance = 50.0
            val raySize = 0.1
            val eyeLocation = player.eyeLocation.clone().add(player.eyeLocation.direction)
            val rayTrace = player.world.rayTrace(
                eyeLocation,
                eyeLocation.direction,
                maxDistance,
                FluidCollisionMode.NEVER,
                true,
                raySize,
                null
            )
            val location = rayTrace?.hitEntity?.location ?: rayTrace?.hitBlock?.location ?: return

            player.world.strikeLightning(location)

            if (player.gameMode == GameMode.CREATIVE) return

            item.amount -= 1
        }
    }
}
