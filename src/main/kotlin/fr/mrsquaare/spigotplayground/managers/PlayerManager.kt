package fr.mrsquaare.spigotplayground.managers

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.models.MyPlayer
import fr.mrsquaare.spigotplayground.packets.npc.NPCInteractPacketHandler
import java.util.*

class PlayerManager(private val plugin: SpigotPlaygroundPlugin) {
    private val players = mutableMapOf<UUID, MyPlayer>()

    fun getPlayer(uniqueId: UUID): MyPlayer? = players[uniqueId]

    fun addPlayer(myPlayer: MyPlayer) {
        myPlayer.addPacketHandlers(listOf(NPCInteractPacketHandler(plugin, myPlayer.entity)))

        players[myPlayer.entity.uniqueId] = myPlayer
    }

    fun removePlayer(uniqueId: UUID) {
        val player = players.remove(uniqueId) ?: return

        player.removePacketHandlers()
    }

    fun registerPlayers() {
        plugin.server.onlinePlayers.forEach { player ->
            addPlayer(MyPlayer(player))
        }
    }

    fun unregisterPlayers() {
        players.forEach { (uniqueId) ->
            removePlayer(uniqueId)
        }
    }
}
