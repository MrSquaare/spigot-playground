package fr.mrsquaare.spigotplayground.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
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
import org.bukkit.entity.Player as BukkitPlayer
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.craftbukkit.v1_20_R2.CraftServer
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

class NPC(id: Int?, uuid: UUID, name: String, private val server: Server, private val world: World, location: Location) {
    val entity: ServerPlayer

    constructor(name: String, server: Server, world: World, location: Location) : this(null, UUID.randomUUID(), name, server, world, location)

    init {
        val nmsServer = (server as CraftServer).server
        val nmsLevel = (world as CraftWorld).handle
        val gameProfile = GameProfile(
            uuid, name
        )
        val ci = ClientInformation.createDefault()

        entity = ServerPlayer(nmsServer, nmsLevel, gameProfile, ci)

        entity.id = id ?: entity.id
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

    fun spawn(player: BukkitPlayer) {
        val nmsPlayer = (player as CraftPlayer).handle

        nmsPlayer.connection.send(
            ClientboundPlayerInfoUpdatePacket(
                ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entity
            )
        )
        nmsPlayer.connection.send(ClientboundAddEntityPacket(entity))
        nmsPlayer.connection.send(ClientboundSetEntityDataPacket(entity.id, entity.entityData.nonDefaultValues))
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

    private val MOJANG_PROFILE_API = "https://api.mojang.com/users/profiles/minecraft"
    private val MOJANG_SESSION_PROFILE_API = "https://sessionserver.mojang.com/session/minecraft/profile"
    private val httpClient = HttpClient.newBuilder().build()

    fun setSkin(name: String) {
        val profileRequest = HttpRequest.newBuilder()
            .uri(URI.create("$MOJANG_PROFILE_API/$name"))
            .GET()
            .header("Content-Type", "application/json")
            .build()
        val profileResponse = httpClient.send(
            profileRequest,
            HttpResponse.BodyHandlers.ofString()
        )

        if (profileResponse.statusCode() != 200) {
            throw Exception("Invalid status code: ${profileResponse.statusCode()}")
        }

        val profileResponseJson = Gson().fromJson(profileResponse.body(), JsonObject::class.java)
        val profileUUID = profileResponseJson.get("id").asString

        val sessionRequest = HttpRequest.newBuilder()
            .uri(URI.create("$MOJANG_SESSION_PROFILE_API/$profileUUID?unsigned=false"))
            .GET()
            .header("Content-Type", "application/json")
            .build()
        val sessionResponse = httpClient.send(
            sessionRequest,
            HttpResponse.BodyHandlers.ofString()
        )

        if (sessionResponse.statusCode() != 200) {
            throw Exception("Invalid status code: ${sessionResponse.statusCode()}")
        }

        val sessionResponseJson = GsonBuilder().create().fromJson(sessionResponse.body(), JsonObject::class.java)
        val sessionProperties = sessionResponseJson.get("properties").asJsonArray
        val sessionTextures = sessionProperties.find { it.asJsonObject.get("name").asString == "textures" }?.asJsonObject

        if (sessionTextures == null) {
            throw Exception("No textures found")
        }

        entity.gameProfile.properties.put(
            "textures",
            Property(
                "textures",
                sessionTextures.get("value").asString,
                sessionTextures.get("signature").asString
            )
        )

        this.respawn()
    }

    fun toMap(): Map<*, *> {
        return mapOf(
            "id" to entity.id,
            "uuid" to entity.uuid.toString(),
            "name" to entity.gameProfile.name,
            "world" to world.uid.toString(),
            "x" to entity.x,
            "y" to entity.y,
            "z" to entity.z
        )
    }

    companion object {
        @JvmStatic
        fun fromMap(map: Map<*, *>, server: Server): NPC? {
            val id = map["id"] as Int? ?: return null
            val uuidStr = map["uuid"] as String? ?: return null
            val uuid = UUID.fromString(uuidStr)
            val name = map["name"] as String? ?: return null
            val worldUuidStr = map["world"] as String? ?: return null
            val worldUUid = UUID.fromString(worldUuidStr)
            val world = server.getWorld(worldUUid) ?: return null
            val x = map["x"] as Double? ?: return null
            val y = map["y"] as Double? ?: return null
            val z = map["z"] as Double? ?: return null
            val location = Location(world, x, y, z)

            return NPC(id, uuid, name, server, world, location)
        }
    }
}
