package io.github.olxmute.watchdog.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("watchdogs")
data class WatchdogsConfig(
    val expats: WatchdogConfig,
)

data class WatchdogConfig(
    val baseUrl: String,
    val searchUrl: String,
)