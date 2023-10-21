package fr.mrsquaare.spigotplayground.listeners

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.listeners.global.PlayerJoinListener
import fr.mrsquaare.spigotplayground.listeners.global.PlayerListener
import fr.mrsquaare.spigotplayground.listeners.global.PlayerQuitListener
import fr.mrsquaare.spigotplayground.listeners.hub.HubDoubleJumpListener
import fr.mrsquaare.spigotplayground.listeners.hub.HubListener

class ListenerManager(private val plugin: SpigotPlaygroundPlugin) {
    private val listeners = mutableListOf(
        PlayerListener(plugin),
        PlayerJoinListener(plugin),
        PlayerQuitListener(plugin),
        HubListener(plugin),
        HubDoubleJumpListener(plugin),
    )

    fun registerListeners() {
        listeners.forEach { plugin.server.pluginManager.registerEvents(it, plugin) }
    }
}
