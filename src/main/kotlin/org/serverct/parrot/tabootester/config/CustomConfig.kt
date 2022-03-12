package org.serverct.parrot.tabootester.config

import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object CustomConfig {

    @Config("Custom.yml")
    lateinit var config: Configuration
        private set

}