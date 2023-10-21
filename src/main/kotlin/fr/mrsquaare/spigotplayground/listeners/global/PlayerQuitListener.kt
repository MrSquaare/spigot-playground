package fr.mrsquaare.spigotplayground.listeners.global

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.listeners.BaseListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(plugin: SpigotPlaygroundPlugin) : BaseListener(plugin) {
    override val name = "PlayerQuitListener"

    private val message = plugin.config.getString("events.quit.message")

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (message == null) return

        event.quitMessage = message.replace("%player%", event.player.name)
    }
}
