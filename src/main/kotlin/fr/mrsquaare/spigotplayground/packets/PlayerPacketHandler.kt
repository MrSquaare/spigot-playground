package fr.mrsquaare.spigotplayground.packets

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import org.bukkit.entity.Player

abstract class PlayerPacketHandler<I>(plugin: SpigotPlaygroundPlugin, protected val player: Player) :
    BasePacketHandler<I>(plugin) {
}
