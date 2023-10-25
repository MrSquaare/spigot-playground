package fr.mrsquaare.spigotplayground.items.hub

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.events.hub.HubPlayerJoinEvent
import fr.mrsquaare.spigotplayground.items.BaseItem
import fr.mrsquaare.spigotplayground.items.ListenableItem
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.metadata.FixedMetadataValue
import kotlin.math.min
import kotlin.math.sqrt

class HubExplosiveSnowballItem(plugin: SpigotPlaygroundPlugin) : BaseItem(plugin), ListenableItem {
    override val stack = Stack(plugin)

    class Stack(plugin: SpigotPlaygroundPlugin) : BaseItem.Stack(
        plugin = plugin,
        name = "explosive_snowball",
        displayName = "Explosive Snowball",
        material = Material.SNOWBALL
    )

    private val hubWorldName = plugin.config.getString("worlds.hub")
    private val entityMetadataKey = "explosive_snowball"
    private val entityMetadataKeyGiveBack = "explosive_snowball_give_back"

    override val listen = Listen()

    inner class Listen : Listener {
        @EventHandler
        fun onPlayerJoinHub(event: HubPlayerJoinEvent) {
            val player = event.player

            player.inventory.addItem(stack)
        }

        @EventHandler
        fun onProjectileLaunch(event: ProjectileLaunchEvent) {
            if (event.entity.world.name != hubWorldName) return

            val entity = event.entity

            if (entity !is Snowball) return
            if (!stack.isItem(entity.item)) return

            entity.setMetadata(entityMetadataKey, FixedMetadataValue(plugin, null))

            val shooter = entity.shooter

            if (shooter is Player && shooter.gameMode != GameMode.CREATIVE) {
                entity.setMetadata(entityMetadataKeyGiveBack, FixedMetadataValue(plugin, null))
            }
        }

        @EventHandler
        fun onProjectileHit(event: ProjectileHitEvent) {
            if (event.entity.world.name != hubWorldName) return

            val entity = event.entity

            if (entity !is Snowball) return
            if (!entity.hasMetadata(entityMetadataKey)) return

            entity.world.createExplosion(entity.location, 4f, false, false, entity)

            val shooter = entity.shooter

            if (shooter is Player && entity.hasMetadata(entityMetadataKeyGiveBack)) {
                shooter.inventory.addItem(entity.item)
            }
        }

        @EventHandler
        fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
            if (event.damager.world.name != hubWorldName) return

            val damager = event.damager

            if (damager !is Snowball) return
            if (!damager.hasMetadata(entityMetadataKey)) return

            event.isCancelled = true

            if (event.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return

            val radius = 5.0
            val maxStrength = 2.0
            val entities = damager.getNearbyEntities(radius, radius, radius)

            entities.forEach { entity ->
                val distance = entity.location.distance(damager.location)
                val direction = entity.location.toVector().subtract(damager.location.toVector()).normalize()
                val strength = maxStrength * (1 - min(distance, radius) / radius)

                direction.multiply(strength)
                direction.y = sqrt(strength / 2)

                entity.velocity = direction
            }
        }
    }
}
