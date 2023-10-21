package fr.mrsquaare.spigotplayground.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin

class HelloCommand(plugin: SpigotPlaygroundPlugin) : BaseCommand(plugin) {
    override val name = "hello"
    override val description = "Hello, World!"
    override val aliases = listOf("hi")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage("Hello, ${sender.name}!")

        return true
    }
}
