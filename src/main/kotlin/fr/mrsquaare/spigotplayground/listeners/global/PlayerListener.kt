package fr.mrsquaare.spigotplayground.listeners.global

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.listeners.BaseListener
import fr.mrsquaare.spigotplayground.models.MyPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener(plugin: SpigotPlaygroundPlugin) : BaseListener(plugin) {
    override val name = "PlayerListener"

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        plugin.playerManager.addPlayer(MyPlayer(event.player))
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.playerManager.removePlayer(event.player.uniqueId)
    }
}
