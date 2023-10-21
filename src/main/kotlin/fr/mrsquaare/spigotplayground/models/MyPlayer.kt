package fr.mrsquaare.spigotplayground.models

import fr.mrsquaare.spigotplayground.packets.BasePacketHandler
import fr.mrsquaare.spigotplayground.utilities.Reflector
import io.netty.channel.Channel
import net.minecraft.network.Connection
import net.minecraft.server.network.ServerCommonPacketListenerImpl
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer
import org.bukkit.entity.Player

class MyPlayer(val entity: Player) {
    private val channel: Channel
    private var packetHandlers = listOf<BasePacketHandler<*>>()

    init {
        val nmsPlayer = (entity as CraftPlayer).handle
        val nmsConnection = Reflector.getPrivateField<Connection>(
            nmsPlayer.connection, "c", ServerCommonPacketListenerImpl::class.java
        ) ?: error("Could not get connection from player")

        channel = nmsConnection.channel
    }

    fun addPacketHandlers(packetHandlers: List<BasePacketHandler<*>>) {
        this.packetHandlers = packetHandlers

        val channelPipeline = channel.pipeline()

        this.packetHandlers.forEach { packetHandler ->
            if (channelPipeline.get(packetHandler.name) != null) return

            channelPipeline.addAfter("decoder", packetHandler.name, packetHandler)
        }
    }

    fun removePacketHandlers() {
        val channelPipeline = channel.pipeline()

        packetHandlers.forEach { packetHandler ->
            if (channelPipeline.get(packetHandler.name) == null) return

            channelPipeline.remove(packetHandler)
        }

        packetHandlers = listOf()
    }
}
