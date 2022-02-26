package org.serverct.parrot.tabootester.event

import org.bukkit.entity.Item
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ItemDespawnEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object ItemDespawnListener {

//    @SubscribeEvent
    fun onItemDespawn(event: ItemDespawnEvent) {
        """
            ItemDespawnEvent {
                Entity: ${event.entityType}
                Location: ${event.location}
                Item: ${event.entity.itemStack}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

//    @SubscribeEvent
    fun onItemEntityDamaged(event: EntityDamageEvent) {
        val item = event.entity as? Item ?: return

        """
            EntityDamageEvent(for Item) {
                Cause: ${event.cause}
                Location: ${item.location}
                Item: ${item.itemStack}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

}