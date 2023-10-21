package fr.mrsquaare.spigotplayground.utilities

import net.minecraft.network.Connection
import net.minecraft.network.PacketListener
import net.minecraft.network.PacketSendListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import java.net.SocketAddress

// Converted from https://raw.githubusercontent.com/CitizensDev/Citizens2/05048be1f77b92b38c56ee434e2dd5b3d4b95297/v1_20_R2/src/main/java/net/citizensnpcs/nms/v1_20_R2/network/EmptyConnection.java
class EmptyConnection(protocolDirection: PacketFlow?) : Connection(protocolDirection) {
    init {
        channel = EmptyChannel(null)
        address = object : SocketAddress() {
            private val serialVersionUID = 8207338859896320185L
        }
    }

    override fun isConnected(): Boolean {
        return true
    }

    override fun send(packet: Packet<*>?, genericfuturelistener: PacketSendListener?) {}

    override fun setListener(pl: PacketListener?) {
        try {
            val plField = Connection::class.java.getDeclaredField("q")
            plField.isAccessible = true
            plField.set(this, pl)

            val dlField = Connection::class.java.getDeclaredField("p")
            dlField.isAccessible = true
            dlField.set(this, null)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
