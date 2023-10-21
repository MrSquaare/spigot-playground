package fr.mrsquaare.spigotplayground.listeners.hub

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.events.hub.HubPlayerJoinEvent
import fr.mrsquaare.spigotplayground.listeners.BaseListener
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerJoinEvent

class HubListener(plugin: SpigotPlaygroundPlugin) : BaseListener(plugin) {
    override val name = "HubListener"

    private val hubWorldName = plugin.config.getString("worlds.hub")

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        checkPlayerJoinHub(event)
    }

    @EventHandler
    fun onPlayerChangeWorld(event: PlayerChangedWorldEvent) {
        checkPlayerJoinHub(event)
    }

    private fun checkPlayerJoinHub(event: PlayerEvent) {
        if (event.player.world.name != hubWorldName) return

        val newEvent = HubPlayerJoinEvent(event.player)

        plugin.server.pluginManager.callEvent(newEvent)
    }

    @EventHandler
    fun onPlayerJoinHub(event: HubPlayerJoinEvent) {
        if (event.player.world.name != hubWorldName) return

        event.player.sendMessage("Welcome to the hub!")
        event.player.inventory.clear()
        event.player.foodLevel = 20
        event.player.level = 0
        event.player.exp = 0.0f
        event.player.health = 20.0
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if (event.entity.world.name != hubWorldName) return
        if (event.entity !is Player) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        if (event.entity.world.name != hubWorldName) return
        if (event.entity !is Player) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityAirChange(event: EntityAirChangeEvent) {
        if (event.entity.world.name != hubWorldName) return
        if (event.entity !is Player) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        if (event.entity.world.name != hubWorldName) return
        if (event.target !is Player) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityChangeBlock(event: EntityChangeBlockEvent) {
        if (event.entity.world.name != hubWorldName) return

        event.isCancelled = true
    }

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        if (event.entity.world.name != hubWorldName) return

        event.blockList().clear()
    }

    @EventHandler
    fun onEntityPortal(event: EntityPortalEvent) {
        if (event.entity.world.name != hubWorldName) return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.block.world.name != hubWorldName) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.world.name != hubWorldName) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        event.isCancelled = true
    }
}
