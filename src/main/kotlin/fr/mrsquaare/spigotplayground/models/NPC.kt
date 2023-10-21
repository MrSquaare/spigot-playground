package fr.mrsquaare.spigotplayground.models

import com.mojang.authlib.GameProfile
import fr.mrsquaare.spigotplayground.utilities.EmptyConnection
import fr.mrsquaare.spigotplayground.utilities.EmptyConnectionListener
import fr.mrsquaare.spigotplayground.utilities.Reflector
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.world.entity.player.Player
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.craftbukkit.v1_20_R2.CraftServer
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld
import java.util.*

class NPC(name: String, private val server: Server, private val world: World, location: Location) {
    val entity: ServerPlayer

    init {
        val nmsServer = (server as CraftServer).server
        val nmsLevel = (world as CraftWorld).handle
        val gameProfile = GameProfile(
            UUID.randomUUID(), name
        )
        val ci = ClientInformation.createDefault()

        entity = ServerPlayer(nmsServer, nmsLevel, gameProfile, ci)

        entity.setPos(
            location.x,
            location.y,
            location.z
        )

        val npcConnection = EmptyConnection(PacketFlow.CLIENTBOUND)
        val npcCLC = CommonListenerCookie(entity.gameProfile, 0, entity.clientInformation())
        val npcConnectionListener = EmptyConnectionListener(nmsServer, npcConnection, entity, npcCLC)

        entity.connection = npcConnectionListener
    }

    fun spawn() {
        val nmsServer = (server as CraftServer).server

        nmsServer.playerList.broadcastAll(
            ClientboundPlayerInfoUpdatePacket(
                ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entity
            )
        )
        nmsServer.playerList.broadcastAll(ClientboundAddEntityPacket(entity))
        nmsServer.playerList.broadcastAll(ClientboundSetEntityDataPacket(entity.id, entity.entityData.nonDefaultValues))
    }

    fun despawn() {
        val nmsServer = (server as CraftServer).server

        nmsServer.playerList.broadcastAll(ClientboundPlayerInfoRemovePacket(listOf(entity.uuid)))
        nmsServer.playerList.broadcastAll(ClientboundRemoveEntitiesPacket(entity.id))
    }

    fun respawn() {
        this.despawn()
        this.spawn()
    }

    fun rename(name: String) {
        val newGameProfile = GameProfile(
            entity.uuid, name
        )

        newGameProfile.properties.putAll(entity.gameProfile.properties)

        Reflector.setPrivateField(entity, "cr", newGameProfile, Player::class.java)
        entity.displayName = entity.gameProfile.name

        this.respawn()
    }
}
