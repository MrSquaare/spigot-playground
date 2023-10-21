package fr.mrsquaare.spigotplayground.utilities

import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.server.network.ServerGamePacketListenerImpl

// Converted from https://raw.githubusercontent.com/CitizensDev/Citizens2/05048be1f77b92b38c56ee434e2dd5b3d4b95297/v1_20_R2/src/main/java/net/citizensnpcs/nms/v1_20_R2/network/EmptyPacketListener.java
class EmptyConnectionListener(minecraftServer: MinecraftServer?, networkManager: Connection?, entityPlayer: ServerPlayer?, clc: CommonListenerCookie?) : ServerGamePacketListenerImpl(minecraftServer, networkManager, entityPlayer, clc) {
    override fun send(packet: Packet<*>?) {}
}
