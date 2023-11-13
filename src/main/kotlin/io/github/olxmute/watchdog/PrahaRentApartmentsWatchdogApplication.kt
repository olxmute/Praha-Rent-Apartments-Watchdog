package io.github.olxmute.watchdog

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import org.telegram.telegrambots.starter.TelegramBotStarterConfiguration

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
@ImportAutoConfiguration(TelegramBotStarterConfiguration::class)
class PrahaRentApartmentsWatchdogApplication

fun main(args: Array<String>) {
    runApplication<PrahaRentApartmentsWatchdogApplication>(*args)
}
