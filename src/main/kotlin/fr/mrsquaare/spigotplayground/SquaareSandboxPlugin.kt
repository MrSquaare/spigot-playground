package fr.mrsquaare.spigotplayground

import fr.mrsquaare.spigotplayground.commands.CommandManager
import fr.mrsquaare.spigotplayground.items.ItemManager
import fr.mrsquaare.spigotplayground.listeners.ListenerManager
import fr.mrsquaare.spigotplayground.managers.NPCManager
import fr.mrsquaare.spigotplayground.managers.PlayerManager
import org.bukkit.plugin.java.JavaPlugin

class SpigotPlaygroundPlugin : JavaPlugin() {
    private val commandManager = CommandManager(this)
    private val listenerManager = ListenerManager(this)
    private val itemManager = ItemManager(this)
    val playerManager = PlayerManager(this)
    val npcManager = NPCManager(this)

    override fun onEnable() {
        logger.info("${description.name} has been enabled!");

        saveDefaultConfig()
        commandManager.registerCommands()
        listenerManager.registerListeners()
        itemManager.registerItems()
        playerManager.registerPlayers()
    }

    override fun onDisable() {
        playerManager.unregisterPlayers()

        logger.info("${description.name} has been disabled!");
    }
}
