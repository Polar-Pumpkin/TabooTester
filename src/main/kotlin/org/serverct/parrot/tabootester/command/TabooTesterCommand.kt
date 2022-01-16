package org.serverct.parrot.tabootester.command

import org.apache.commons.lang.math.NumberUtils
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import org.serverct.parrot.tabootester.util.rangeSurface
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.sendInfoMessage
import taboolib.platform.util.sendWarnMessage
import kotlin.math.roundToInt

@CommandHeader("tabootester", aliases = ["libtest", "tt"])
object TabooTesterCommand {

    @CommandBody
    val range = subCommand {
        dynamic {
            execute<Player> { user, context, _ ->
                val range = NumberUtils.toInt(context.argument(0), -1)
                if (range <= 0) {
                    user.sendWarnMessage("半径需要一个正整数.")
                    return@execute
                }

                user.location.rangeSurface(range).forEach {
                    it.block.type = Material.GLASS
                }
            }
        }
    }

    @CommandBody
    val cloud = subCommand {
        dynamic {
            execute<Player> { user, context, _ ->
                val range = context.argument(0).toDoubleOrNull() ?: -1.0
                if (range <= 0.0) {
                    return@execute user.sendWarnMessage("半径需要一个正整数.")
                }

                user.world.spawn(user.location, AreaEffectCloud::class.java) {
                    it.setMetadata("Gas", FixedMetadataValue(BukkitPlugin.getInstance(), 3))

                    it.radius = range.toFloat()
                    it.duration = 3 * 20
                    it.reapplicationDelay = (0.5 * 20).roundToInt()
                    it.waitTime = (0.5 * 20).roundToInt()

                    it.radiusOnUse = 0.0F
                    it.radiusPerTick = 0.0F
                    it.durationOnUse = 0

                    it.particle = Particle.SPELL
                    it.color = Color.GREEN
                    it.source = user

                    it.basePotionData = PotionData(PotionType.WATER)
                }
                user.sendInfoMessage("Tagged AreaEffectCloud spawned.")
            }
        }
    }

}