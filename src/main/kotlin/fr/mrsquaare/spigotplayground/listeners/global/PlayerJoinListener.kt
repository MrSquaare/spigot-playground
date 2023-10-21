package fr.mrsquaare.spigotplayground.listeners.global

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.listeners.BaseListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(plugin: SpigotPlaygroundPlugin) : BaseListener(plugin) {
    override val name = "PlayerJoinListener"

    private val message = plugin.config.getString("events.join.message")

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (message == null) return

        event.joinMessage = message.replace("%player%", event.player.name)
    }
}
