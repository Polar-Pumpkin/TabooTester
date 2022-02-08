package org.serverct.parrot.tabootester.event

import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object EntitySpawnListener {

    @SubscribeEvent
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        """
            CreatureSpawnEvent {
                Entity: ${event.entityType}
                SpawnReason: ${event.spawnReason}
                Location: ${event.location}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

    @SubscribeEvent
    fun onEntitySpawn(event: EntitySpawnEvent) {
        """
            EntitySpawnEvent {
                Entity: ${event.entityType}
                Location: ${event.location}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

    @SubscribeEvent
    fun onHangingEntitySpawn(event: HangingPlaceEvent) {
        """
            HangingPlaceEvent {
                Hanging: ${event.entity.type}
            }
        """.trimIndent().split('\n').forEach { info(it) }
    }

}