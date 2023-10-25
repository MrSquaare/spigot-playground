package fr.mrsquaare.spigotplayground.packets.npc

import fr.mrsquaare.spigotplayground.SpigotPlaygroundPlugin
import fr.mrsquaare.spigotplayground.events.npc.NPCPlayerInteractEvent
import fr.mrsquaare.spigotplayground.packets.PlayerPacketHandler
import fr.mrsquaare.spigotplayground.utilities.Reflector
import io.netty.channel.ChannelHandlerContext
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class NPCInteractPacketHandler(plugin: SpigotPlaygroundPlugin, player: Player) :
    PlayerPacketHandler<ServerboundInteractPacket>(plugin, player) {
    override val name = "npc_packet_handler"

    override fun decode(ctx: ChannelHandlerContext, msg: ServerboundInteractPacket, out: MutableList<Any>) {
        val entityId = Reflector.getPrivateField<Int>(msg, "a") ?: return
        val action = Reflector.getPrivateField<Any>(msg, "b") ?: return
        val actionType = Reflector.getPrivateMethod<Any>(action, "a") ?: return
        val actionTypeStr = actionType.toString()
        val interactionAction = if (actionTypeStr != "ATTACK") Reflector.getPrivateField<Any>(
            action, "a"
        ) else null
        val interactionActionStr = interactionAction?.toString()

        if (actionTypeStr == "INTERACT_AT" && interactionActionStr == "MAIN_HAND") {
            val npc = plugin.npcManager.get(entityId) ?: return

            object : BukkitRunnable() {
                override fun run() {
                    plugin.server.pluginManager.callEvent(
                        NPCPlayerInteractEvent(
                            npc,
                            player,
                            player.inventory.itemInMainHand
                        )
                    )
                }
            }.runTask(plugin)
        }

        out.add(msg)
    }
}
