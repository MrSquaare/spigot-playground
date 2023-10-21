package fr.mrsquaare.spigotplayground.listeners.hub

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.events.hub.HubPlayerJoinEvent
import fr.mrsquaare.spigotplayground.listeners.BaseListener
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent

class HubDoubleJumpListener(plugin: SpigotPlaygroundPlugin) : BaseListener(plugin) {
    override val name = "HubDoubleJumpListener"

    private val hubWorldName = plugin.config.getString("worlds.hub")

    @EventHandler
    fun onPlayerJoinHub(event: HubPlayerJoinEvent) {
        event.player.allowFlight = true
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.player.world.name != hubWorldName) return
        if (event.player.gameMode == GameMode.CREATIVE) return
        if (event.player.allowFlight) return

        val to = event.to ?: return

        val block = event.player.world.getBlockAt(to.blockX, to.blockY - 1, to.blockZ)

        if (!block.type.isSolid || to.y % 1 != 0.0) return

        event.player.allowFlight = true
    }

    @EventHandler
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        if (event.player.world.name != hubWorldName) return
        if (event.player.gameMode == GameMode.CREATIVE) return
        if (!event.player.allowFlight) return

        event.isCancelled = true
        event.player.allowFlight = false
        event.player.velocity = event.player.location.direction.multiply(1.5).setY(1.0)

        event.player.playSound(event.player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f)
        event.player.spawnParticle(Particle.FIREWORKS_SPARK, event.player.location, 20, 1.0, 1.0, 1.0, 0.0)
    }
}
