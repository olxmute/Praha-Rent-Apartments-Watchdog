package io.github.olxmute.watchdog.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("bot")
data class BotConfig(
    val chatId: String,
    val token: String,
    val username: String,
)
