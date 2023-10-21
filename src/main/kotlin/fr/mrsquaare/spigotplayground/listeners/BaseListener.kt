package fr.mrsquaare.spigotplayground.listeners

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import org.bukkit.event.Listener

abstract class BaseListener(protected val plugin: SpigotPlaygroundPlugin) : Listener {
    abstract val name: String
}
