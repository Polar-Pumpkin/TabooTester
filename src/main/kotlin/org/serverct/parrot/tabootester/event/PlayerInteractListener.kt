package org.serverct.parrot.tabootester.event

import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object PlayerInteractListener {

//    @SubscribeEvent(ignoreCancelled = true)
    fun onInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) {
            return
        }
        """
            PlayerInteractEvent {
                Hand: ${event.hand}
                Action: ${event.action}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

//    @SubscribeEvent
    fun onInteractWithFlowerPot(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        if (block.type != Material.FLOWER_POT) {
            return
        }
        """
            FlowerPot {
                Location: ${block.location}
                State: ${block.state::class.simpleName} @${block.state::class.java.interfaces.joinToString(", ") { it.simpleName }} 
                Data: ${block.blockData::class.simpleName} @${block.blockData::class.java.interfaces.joinToString(", ") { it.simpleName }}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

}