package fr.mrsquaare.spigotplayground.models

import fr.mrsquaare.spigotplayground.packets.BasePacketHandler
import fr.mrsquaare.spigotplayground.utilities.Reflector
import io.netty.channel.ChannelPipeline
import net.minecraft.network.Connection
import net.minecraft.server.network.ServerCommonPacketListenerImpl
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer
import org.bukkit.entity.Player

class MyPlayer(val entity: Player) {
    private val channelPipeline: ChannelPipeline
    private var packetHandlers = listOf<BasePacketHandler<*>>()

    init {
        val nmsPlayer = (entity as CraftPlayer).handle
        val nmsConnection = Reflector.getPrivateField<Connection>(
            nmsPlayer.connection, "c", ServerCommonPacketListenerImpl::class.java
        ) ?: error("Could not get connection from player")

        channelPipeline = nmsConnection.channel.pipeline()

    }

    fun addPacketHandlers(packetHandlers: List<BasePacketHandler<*>>) {
        this.packetHandlers = packetHandlers

        this.packetHandlers.forEach { packetHandler ->
            if (channelPipeline.get(packetHandler.name) != null) return

            channelPipeline.addAfter("decoder", packetHandler.name, packetHandler)
        }
    }

    fun removePacketHandlers() {
        packetHandlers.forEach { packetHandler ->
            channelPipeline.remove(packetHandler)
        }

        packetHandlers = listOf()
    }
}
