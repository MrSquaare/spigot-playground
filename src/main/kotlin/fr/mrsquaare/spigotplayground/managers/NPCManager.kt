package fr.mrsquaare.spigotplayground.managers

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.models.NPC

class NPCManager(private val plugin: SpigotPlaygroundPlugin) {
    private val npcs = mutableMapOf<Int, NPC>()

    fun getNPC(entityId: Int): NPC? = npcs[entityId]

    fun addNPC(npc: NPC) {
        npcs[npc.entity.id] = npc
    }

    fun removeNPC(id: Int) {
        npcs.remove(id)
    }

    fun loadNPCs() {
        TODO()
    }

    fun saveNPCs() {
        TODO()
    }
}
