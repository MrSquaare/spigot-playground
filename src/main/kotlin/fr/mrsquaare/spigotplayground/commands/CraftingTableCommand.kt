package fr.mrsquaare.spigotplayground.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin

class CraftingTableCommand(plugin: SpigotPlaygroundPlugin) : BaseCommand(plugin) {
    override val name = "craftingtable"
    override val description = "Open a crafting table"
    override val aliases = listOf("craft", "ct")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        sender.openWorkbench(null, true)

        return true
    }
}
