package fr.mrsquaare.spigotplayground.commands

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

abstract class BaseCommand(protected val plugin: SpigotPlaygroundPlugin) : TabExecutor {
    abstract val name: String
    abstract val description: String
    open val usageMessage: String? = null
    open val permission: String? = null
    open val permissionMessage: String? = null
    open val aliases: List<String>? = null

    private val computeCommand: () -> Command = {
        val command = object : Command(name) {
            override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
                return onCommand(sender, this, commandLabel, args)
            }

            override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): MutableList<String> {
                return onTabComplete(sender, this, alias, args)
            }
        }

        command.description = description
        command.usage = usageMessage ?: command.usage
        command.permission = permission
        command.permissionMessage = permissionMessage
        command.aliases = aliases ?: command.aliases

        command
    }

    val command by lazy(computeCommand)

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        return mutableListOf()
    }
}
