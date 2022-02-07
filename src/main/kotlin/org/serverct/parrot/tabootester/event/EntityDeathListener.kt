package org.serverct.parrot.tabootester.event

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent

object EntityDeathListener {

    @SubscribeEvent
    fun onDeath(event: EntityDeathEvent) {
        when (event.entity.type) {
            EntityType.ZOMBIE -> {
                event.drops.clear()
                event.drops += ItemStack(Material.DIAMOND)
            }
            else -> event.drops.clear()
        }
    }

}