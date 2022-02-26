package org.serverct.parrot.tabootester.util

import com.google.common.base.Enums
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import java.util.*
import kotlin.math.roundToInt

inline fun <reified T : Enum<T>> String?.toEnum(): T? {
    this ?: return null
    return Enums.getIfPresent(T::class.java, this.uppercase()).orNull()
}

private val around = listOf(
    BlockFace.EAST,
    BlockFace.WEST,
    BlockFace.SOUTH,
    BlockFace.NORTH,
    BlockFace.NORTH_EAST,
    BlockFace.NORTH_WEST,
    BlockFace.SOUTH_EAST,
    BlockFace.SOUTH_WEST
)

fun Location.rangeSurface(range: Int): List<Location> {
    var surface = block
    // 如果基位置不是地面, 就要垂直下沉直到接触到地面
    while (!(surface.type != Material.AIR && surface.getRelative(BlockFace.UP).type == Material.AIR)) {
        surface = surface.getRelative(BlockFace.DOWN)
        // 如果下沉到超出范围还没接触到地面, 直接搜寻失败返回空列表 FIXME 可以在这里继续优化寻找球体范围内的地面
        if (surface.location.distance(this).roundToInt() > range) {
            return emptyList()
        }
    }

    // 用递归爬周围的表面方块
    fun Block.collectAround(center: Location, range: Int, collector: MutableList<Location>): List<Location> {
        around.forEach { face ->
            // 获取周围的方块
            val near = getRelative(face)
            // 如果是悬空的就不爬了 FIXME 可以在这里再下沉以优化算法
            if (near.type == Material.AIR) {
                return@forEach
            }
            // 如果超出了基点范围就不爬了
            if (near.location.distance(center).roundToInt() > range) {
                return@forEach
            }

            // 摸摸上方是不是空气 (该位置是不是地面)
            val above = near.getRelative(BlockFace.UP)
            if (above.type == Material.AIR) {
                val location = above.location
                // 看看这个位置是不是已经爬过了
                if (location !in collector) {
                    collector.add(location)
                    // 爬完了这个就继续爬它旁边
                    near.collectAround(center, range, collector)
                }
            } else {
                // 如果不是就往上爬
                above.collectAround(center, range, collector)
            }
        }
        return collector
    }
    // 从刚刚找到的基点的接地面投影开始爬周围的地面, 先把接地面上方那个坐标算进去 :D
    return surface.collectAround(this, range, ArrayList<Location>().apply {
        add(surface.getRelative(BlockFace.UP).location)
    })
}

fun Location.squareBorder(radius: Int): List<Location> {
    val queue = LinkedList<Location>()

    val y = blockY.toDouble()
    val west = blockX - radius
    val east = blockX + radius
    val north = blockZ - radius
    val south = blockZ + radius

    for (z in north..south) {
        queue.offer(Location(world, west.toDouble(), y, z.toDouble()))
        queue.offer(Location(world, east.toDouble(), y, z.toDouble()))
    }

    for (x in (west + 1) until east) {
        queue.offer(Location(world, x.toDouble(), y, north.toDouble()))
        queue.offer(Location(world, x.toDouble(), y, south.toDouble()))
    }
    return queue
}