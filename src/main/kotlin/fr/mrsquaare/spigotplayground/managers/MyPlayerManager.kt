package fr.mrsquaare.spigotplayground.managers

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.models.MyPlayer
import fr.mrsquaare.spigotplayground.packets.npc.NPCInteractPacketHandler
import java.util.*

class MyPlayerManager(private val plugin: SpigotPlaygroundPlugin) {
    private val myPlayers = mutableMapOf<UUID, MyPlayer>()

    fun get(uniqueId: UUID): MyPlayer? = myPlayers[uniqueId]

    fun add(myPlayer: MyPlayer) {
        myPlayer.addPacketHandlers(listOf(NPCInteractPacketHandler(plugin, myPlayer.entity)))

        myPlayers[myPlayer.entity.uniqueId] = myPlayer
    }

    fun remove(uniqueId: UUID) {
        val player = myPlayers.remove(uniqueId) ?: return

        player.removePacketHandlers()
    }

    fun registerPlayers() {
        plugin.server.onlinePlayers.forEach { player ->
            add(MyPlayer(player))
        }
    }

    fun unregisterPlayers() {
        myPlayers.forEach { (uniqueId) ->
            remove(uniqueId)
        }
    }
}
