package io.github.olxmute.watchdog.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("watchdogs")
data class WatchdogsConfig(
    val expats: WatchdogConfig,
)

data class WatchdogConfig(
    val baseUrl: String,
    val searchUrl: String,
)