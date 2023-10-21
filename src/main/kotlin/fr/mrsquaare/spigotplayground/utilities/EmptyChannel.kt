package fr.mrsquaare.spigotplayground.utilities

import io.netty.channel.*
import java.net.SocketAddress

// Converted from https://raw.githubusercontent.com/CitizensDev/Citizens2/05048be1f77b92b38c56ee434e2dd5b3d4b95297/main/src/main/java/net/citizensnpcs/util/EmptyChannel.java
class EmptyChannel(parent: Channel?) : AbstractChannel(parent) {
    private val config: ChannelConfig = DefaultChannelConfig(this)
    override fun config(): ChannelConfig {
        config.setAutoRead(true)

        return config
    }

    @Throws(Exception::class)
    override fun doBeginRead() {
    }

    @Throws(Exception::class)
    override fun doBind(arg0: SocketAddress) {
    }

    @Throws(Exception::class)
    override fun doClose() {
    }

    @Throws(Exception::class)
    override fun doDisconnect() {
    }

    @Throws(Exception::class)
    override fun doWrite(arg0: ChannelOutboundBuffer) {
    }

    override fun isActive(): Boolean {
        return false
    }

    override fun isCompatible(arg0: EventLoop): Boolean {
        return false
    }

    override fun isOpen(): Boolean {
        return false
    }

    override fun localAddress0(): SocketAddress? {
        return null
    }

    override fun metadata(): ChannelMetadata {
        return ChannelMetadata(true)
    }

    override fun newUnsafe(): AbstractUnsafe? {
        return null
    }

    override fun remoteAddress0(): SocketAddress? {
        return null
    }
}
