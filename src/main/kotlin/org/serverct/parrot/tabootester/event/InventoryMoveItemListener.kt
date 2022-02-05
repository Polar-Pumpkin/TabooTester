package org.serverct.parrot.tabootester.event

import org.bukkit.event.inventory.InventoryMoveItemEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object InventoryMoveItemListener {

    @SubscribeEvent(ignoreCancelled = true)
    fun onHopper(event: InventoryMoveItemEvent) {
        """
            InventoryMoveItemEvent {
                Source: ${event.source.holder}
                Destination: ${event.destination.holder}
                ItemStack: ${event.item}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

}