package org.serverct.parrot.tabootester

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object TabooTester : Plugin() {

    override fun onEnable() {
        info("Successfully running ExamplePlugin!")
    }
}