package org.serverct.parrot.tabootester.event

import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object PlayerInteractListener {

    @SubscribeEvent(ignoreCancelled = true)
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


}