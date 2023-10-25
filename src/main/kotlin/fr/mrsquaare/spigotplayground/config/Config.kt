package fr.mrsquaare.spigotplayground.config

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config(plugin: SpigotPlaygroundPlugin, path: String) {
    val configFile: FileConfiguration

    private val file: File

    init {
        plugin.saveResource(path, false)

        file = File(plugin.dataFolder, path)
        configFile = YamlConfiguration()

        configFile.load(file)
    }

    fun load() {
        configFile.load(file)
    }

    fun save() {
        configFile.save(file)
    }
}
