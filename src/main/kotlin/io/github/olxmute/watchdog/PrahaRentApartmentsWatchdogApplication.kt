package io.github.olxmute.watchdog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
class PrahaRentApartmentsWatchdogApplication

fun main(args: Array<String>) {
	runApplication<PrahaRentApartmentsWatchdogApplication>(*args)
}
