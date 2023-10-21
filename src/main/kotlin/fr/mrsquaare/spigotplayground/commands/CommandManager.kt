package fr.mrsquaare.spigotplayground.commands

import fr.mrsquaare.spigotplayground.utilities.Reflector
import org.bukkit.command.CommandMap
import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin

class CommandManager(private val plugin: SpigotPlaygroundPlugin) {
    private val commands = listOf(
        HelloCommand(plugin),
        CraftingTableCommand(plugin)
    )
    private val commandMap = Reflector.getPrivateField<CommandMap>(plugin.server.pluginManager, "commandMap")
        ?: error("Cannot get commandMap")

    fun registerCommands() {
        commands.forEach { commandMap.register(plugin.name, it.command) }
    }
}
