package fr.mrsquaare.spigotplayground

import fr.mrsquaare.spigotplayground.commands.CommandManager
import fr.mrsquaare.spigotplayground.items.ItemManager
import fr.mrsquaare.spigotplayground.listeners.ListenerManager
import fr.mrsquaare.spigotplayground.managers.NPCManager
import fr.mrsquaare.spigotplayground.managers.MyPlayerManager
import org.bukkit.plugin.java.JavaPlugin

class SpigotPlaygroundPlugin : JavaPlugin() {
    private val commandManager = CommandManager(this)
    private val listenerManager = ListenerManager(this)
    private val itemManager = ItemManager(this)
    val myPlayerManager = MyPlayerManager(this)
    val npcManager = NPCManager(this)

    override fun onEnable() {
        logger.info("${description.name} has been enabled!");

        saveDefaultConfig()
        commandManager.registerCommands()
        listenerManager.registerListeners()
        itemManager.registerItems()
        myPlayerManager.registerPlayers()
        npcManager.loadNPCs()
    }

    override fun onDisable() {
        npcManager.saveNPCs()
        myPlayerManager.unregisterPlayers()

        logger.info("${description.name} has been disabled!");
    }
}
