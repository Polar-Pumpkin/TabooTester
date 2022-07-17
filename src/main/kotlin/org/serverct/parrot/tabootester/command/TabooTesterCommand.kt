package org.serverct.parrot.tabootester.command

import ink.ptms.adyeshach.api.AdyeshachAPI
import ink.ptms.adyeshach.common.entity.EntityTypes
import ink.ptms.adyeshach.common.entity.type.AdySlime
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.command.CommandSender
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import org.serverct.parrot.tabootester.config.CustomConfig
import org.serverct.parrot.tabootester.event.HologramListener
import org.serverct.parrot.tabootester.util.rangeSurface
import org.serverct.parrot.tabootester.util.squareBorder
import org.serverct.parrot.tabootester.util.toEnum
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.common5.Coerce
import taboolib.common5.Demand
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.isAir
import taboolib.platform.util.sendInfoMessage
import taboolib.platform.util.sendWarnMessage
import java.util.concurrent.CompletableFuture
import kotlin.math.roundToInt

@CommandHeader("tabootester", aliases = ["libtest", "tt"])
object TabooTesterCommand {

    @CommandBody
    private val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            CustomConfig.config.reload()
            sender.sendInfoMessage("插件重载完成")
        }
    }

    @CommandBody
    private val range = subCommand {
        dynamic {
            execute<Player> { user, context, _ ->
                val range = context.argument(0).toIntOrNull() ?: -1
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
    private val cloud = subCommand {
        dynamic {
            dynamic {
                suggestion<Player> { _, _ ->
                    Particle.values().map { it.name }
                }
                execute<Player> { user, context, _ ->
                    val range = context.argument(-1).toDoubleOrNull() ?: -1.0
                    if (range <= 0.0) {
                        return@execute user.sendWarnMessage("半径需要一个正整数.")
                    }
                    val particle = context.argument(0).toEnum<Particle>()
                        ?: return@execute user.sendWarnMessage("请指定一个有效的粒子效果")

                    user.world.spawn(user.location, AreaEffectCloud::class.java) {
                        it.setMetadata("Gas", FixedMetadataValue(BukkitPlugin.getInstance(), 3))

                        it.radius = range.toFloat()
                        it.duration = 3 * 20
                        it.reapplicationDelay = (0.5 * 20).roundToInt()
                        it.waitTime = (0.5 * 20).roundToInt()

                        it.radiusOnUse = 0.0F
                        it.radiusPerTick = 0.0F
                        it.durationOnUse = 0

                        it.particle = particle
                        it.color = Color.LIME
                        it.source = user

                        it.basePotionData = PotionData(PotionType.WATER)
                    }
                    user.sendInfoMessage("Tagged AreaEffectCloud spawned.")
                }
            }
        }
    }

    @CommandBody
    private val surface = subCommand {
        dynamic {
            restrict<Player> { _, _, argument -> Coerce.asInteger(argument).orElse(0) > 0 }
            execute<Player> { user, context, _ ->
                val heights = 0 until 255
                fun isSurface(block: Block): Boolean = !block.isEmpty && block.getRelative(BlockFace.UP).isEmpty
                fun closer(block: Block): Block = block.getRelative(if (block.isEmpty) BlockFace.DOWN else BlockFace.UP)
                fun isStandable(location: Location): Boolean = with(location.block) {
                    type.isSolid && getRelative(BlockFace.UP).isEmpty && getRelative(BlockFace.UP, 2).isEmpty
                }

                val timestamp = System.currentTimeMillis()
                val range = context.argument(0).toInt()
                val center = user.location
                submit(async = true) {
                    for (radius in 0..range) {
                        center.squareBorder(radius).forEach { value ->
                            val future = CompletableFuture<Location>()

                            if (value.world == null || value.blockY < 0) {
                                future.completeExceptionally(IllegalArgumentException("Invalid location: $value"))
                            } else {
                                submit(async = true) {
                                    var block = value.block
                                    while (block.y in heights && !isSurface(block)) {
                                        block = closer(block)
                                    }
                                    future.complete(block.location)
                                }
                            }

                            future.whenComplete { at, exception ->
                                exception?.printStackTrace()
                                at ?: return@whenComplete
                                if (isStandable(at)) {
                                    at.block.let {
                                        submit { it.type = Material.GLASS }
                                    }
                                }
                            }
                        }
                    }
                    user.sendInfoMessage("Time elapse: {0}ms", System.currentTimeMillis() - timestamp)
                }
            }
        }
    }

    @CommandBody
    private val hologram = subCommand {
        dynamic {
            execute<Player> { user, _, argument ->
                if (!HologramListener.isHologramEnabled) {
                    return@execute
                }
                val location = user.location
                val hologram = AdyeshachAPI.createHologram(user, location, argument.split(' '))
                val slime = AdyeshachAPI.getEntityManagerPrivateTemporary(user)
                    .create(EntityTypes.SLIME, location.clone().add(0.5, 0.0, 0.0)) {
                        val npc = it as AdySlime
                        npc.setSize(1)
                        npc.setTag("Clickable", "click")
                    } as AdySlime
                HologramListener.holograms += Triple(location, hologram, slime)
                user.sendMessage("Created.")
            }
        }
    }

    @CommandBody
    private val demand = subCommand {
        dynamic("内容") {
            execute<CommandSender> { sender, _, argument ->
                val demand = Demand(argument)
                sender.sendMessage(demand.toString())
            }
        }
    }

    @CommandBody
    private val backpack = subCommand {
        fun Array<ItemStack?>.print(viewer: Player, name: String) {
            val space = count { it.isAir() }
            viewer.sendMessage("${name}(${space}/${size}): ${contentToString()}")
        }

        execute<Player> { user, _, _ ->
            val inv = user.inventory
            inv.contents.print(user, "Contents")
            inv.armorContents.print(user, " ArmorContents")
            inv.extraContents.print(user, "ExtraContents")
            inv.storageContents.print(user, "StorageContents")
        }
    }

    @CommandBody
    private val dust = subCommand {
        dynamic("R") {
            restrict<Player> { _, _, argument -> Coerce.asDouble(argument).map { it >= 0.0 }.orElse(false) }
            dynamic("G") {
                restrict<Player> { _, _, argument -> Coerce.asDouble(argument).map { it >= 0.0 }.orElse(false) }
                dynamic("B") {
                    restrict<Player> { _, _, argument -> Coerce.asDouble(argument).map { it >= 0.0 }.orElse(false) }
                    dynamic("Size") {
                        restrict<Player> { _, _, argument -> Coerce.asDouble(argument).map { it >= 0.0 }.orElse(false) }
                        execute<Player> { user, context, _ ->
                            if (MinecraftVersion.major > 4) {
                                val r = Coerce.toInteger(context.argument(-3))
                                val g = Coerce.toInteger(context.argument(-2))
                                val b = Coerce.toInteger(context.argument(-1))
                                val size = Coerce.toFloat(context.argument(0))
                                val option = DustOptions(Color.fromRGB(r, g, b), size)
                                user.spawnParticle(Particle.REDSTONE, user.location, 20, 1.7, 1.7, 1.7, option)
                            } else {
                                val r = Coerce.toDouble(context.argument(-3))
                                val g = Coerce.toDouble(context.argument(-2))
                                val b = Coerce.toDouble(context.argument(-1))
                                val size = Coerce.toDouble(context.argument(0))
                                user.spawnParticle(Particle.REDSTONE, user.location, 20, r, g, b, size)
                            }
                        }
                    }
                }
            }
        }
    }

}