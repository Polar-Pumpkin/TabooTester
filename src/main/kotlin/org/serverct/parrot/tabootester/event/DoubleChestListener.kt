package org.serverct.parrot.tabootester.event

import org.bukkit.block.Chest
import org.bukkit.block.DoubleChest
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendInfoMessage

object DoubleChestListener {

    @SubscribeEvent
    internal fun onInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) {
            return
        }
        val user = event.player
        val block = event.clickedBlock ?: return
        val state = block.state
        user.sendMessage("")

        user.sendInfoMessage("ClickedBlock Type: ${block.type}")
        user.sendInfoMessage("BlockState: ${state::class.simpleName}")

        val chest = block.state as? Chest ?: return
        val inventory = chest.inventory
        val holder = inventory.holder
        user.sendInfoMessage("Chest Inventory: ${inventory::class.simpleName}")
        user.sendInfoMessage("Chest InventoryHolder: ${if (holder != null) holder::class.simpleName else "null"}")

        if (holder is DoubleChest) {
            user.sendInfoMessage("DoubleChest Location: ${holder.location}")
            user.sendInfoMessage("DoubleChest Left Location: ${holder.leftSide?.inventory?.location}")
            user.sendInfoMessage("DoubleChest Right Location: ${holder.rightSide?.inventory?.location}")
        }
    }

}