package org.serverct.parrot.tabootester.event

import ink.ptms.adyeshach.api.AdyeshachAPI
import ink.ptms.adyeshach.api.Hologram
import ink.ptms.adyeshach.api.event.AdyeshachEntityInteractEvent
import ink.ptms.adyeshach.common.entity.type.AdySlime
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.OptionalEvent
import taboolib.common.platform.event.SubscribeEvent

object HologramListener {

    val isHologramEnabled: Boolean by lazy { Bukkit.getPluginManager().isPluginEnabled("Adyeshach") }
    val holograms: MutableList<Triple<Location, Hologram<*>, AdySlime>> = ArrayList()

    @SubscribeEvent(EventPriority.HIGHEST, true)
    fun onMove(event: PlayerMoveEvent) {
        if (!isHologramEnabled) {
            return
        }
        val from = event.from
        val to = event.to ?: return
        if ((from.world != to.world) || (from.blockX == to.blockX && from.blockZ == to.blockZ)) {
            return
        }

        holograms.asSequence()
            .filter { (center, _, _) -> center.world == to.world && center.distance(to) <= 5 }
            .forEach { (center, _, slime) ->
                val direction = center.toVector().subtract(to.toVector()).normalize()
                // direction.rotateAroundY(Math.PI / 2)
                slime.teleport(center.clone().add(Vector(direction.z, 0.0, -direction.x)))
            }
    }

    @SubscribeEvent(bind = "ink.ptms.adyeshach.api.event.AdyeshachEntityInteractEvent")
    fun onInteract(origin: OptionalEvent) {
        val event = origin.get<AdyeshachEntityInteractEvent>()
        val user = event.player
        val entity = event.entity
        if (entity.hasTag("Clickable")) {
            user.sendMessage("Clicked!")
        }
    }

}