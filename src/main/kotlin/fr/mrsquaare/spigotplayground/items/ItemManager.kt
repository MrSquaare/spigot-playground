package fr.mrsquaare.spigotplayground.items

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.items.global.LightningStickItem
import fr.mrsquaare.spigotplayground.items.hub.HubExplosiveSnowballItem
import fr.mrsquaare.spigotplayground.items.npc.NPCStickItem

class ItemManager(private val plugin: SpigotPlaygroundPlugin) {
    private val items = listOf(
        LightningStickItem(plugin),
        HubExplosiveSnowballItem(plugin),
        NPCStickItem(plugin)
    )

    fun registerItems() {
        items.forEach { item ->
            if (item is CraftableItem) {
                plugin.server.addRecipe(item.craft)
            }

            if (item is ListenableItem) {
                plugin.server.pluginManager.registerEvents(item.listen, plugin)
            }
        }
    }
}
