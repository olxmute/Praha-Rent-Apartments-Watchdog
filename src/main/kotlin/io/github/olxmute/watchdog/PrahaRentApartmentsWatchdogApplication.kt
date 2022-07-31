package io.github.olxmute.watchdog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PrahaRentApartmentsWatchdogApplication

fun main(args: Array<String>) {
	runApplication<PrahaRentApartmentsWatchdogApplication>(*args)
}
