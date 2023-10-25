package fr.mrsquaare.spigotplayground.managers

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.config.Config
import fr.mrsquaare.spigotplayground.models.NPC

class NPCManager(private val plugin: SpigotPlaygroundPlugin) {
    private val npcs = mutableMapOf<Int, NPC>()
    private val config = Config(plugin, "npcs.yml")

    fun get(entityId: Int): NPC? = npcs[entityId]

    fun getAll(): List<NPC> = npcs.values.toList()

    fun add(npc: NPC) {
        npcs[npc.entity.id] = npc
    }

    fun remove(id: Int) {
        npcs.remove(id)
    }

    fun loadNPCs() {
        config.load()

        val npcMapList = config.configFile.getMapList("npcs")

        npcMapList.forEach { npcMap ->
            val npc = NPC.fromMap(npcMap, plugin.server) ?: return@forEach

            add(npc)
        }
    }

    fun saveNPCs() {
        val npcMapList = config.configFile.getMapList("npcs")

        npcMapList.clear()

        npcs.forEach {
            val npcMap = it.value.toMap()

            npcMapList.add(npcMap)
        }

        config.configFile.set("npcs", npcMapList)

        config.save()
    }
}
