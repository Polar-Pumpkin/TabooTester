package org.serverct.parrot.tabootester.event

import org.bukkit.event.entity.AreaEffectCloudApplyEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info

object AreaEffectCloudListener {

    private var last: Long? = null

    @SubscribeEvent
    fun onApply(event: AreaEffectCloudApplyEvent) {
        val entity = event.entity
        if (!entity.hasMetadata("Gas")) {
            return
        }

        val delta = last?.let { System.currentTimeMillis() - it } ?: -1
        info("Apply tagged AreaEffectCloud. (${delta}ms)")
    }

}