package fr.mrsquaare.spigotplayground.packets

import io.netty.handler.codec.MessageToMessageDecoder
import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin

abstract class BasePacketHandler<I>(protected val plugin: SpigotPlaygroundPlugin) : MessageToMessageDecoder<I>() {
    abstract val name: String
}
