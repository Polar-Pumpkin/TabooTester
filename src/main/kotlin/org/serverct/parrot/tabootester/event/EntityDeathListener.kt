package org.serverct.parrot.tabootester.event

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object EntityDeathListener {

    @SubscribeEvent
    fun onDeath(event: EntityDeathEvent) {
        """
            EntityDeathEvent {
                Entity: ${event.entityType}
                Experience: ${event.droppedExp}
                Drops: ${event.drops.size}
            }
        """.trimIndent().split('\n').forEach { info(it) }

        when (event.entity.type) {
            EntityType.ZOMBIE -> {
                event.drops.clear()
                event.drops += ItemStack(Material.DIAMOND)
            }
            else -> event.drops.clear()
        }
    }

}