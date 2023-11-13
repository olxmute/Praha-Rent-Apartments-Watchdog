package io.github.olxmute.watchdog

import io.github.olxmute.watchdog.bot.MessageSender
import io.github.olxmute.watchdog.expats.ExpatsWatchdogFacade
import java.util.concurrent.TimeUnit
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WatchdogScheduler(
    private val expatsWatchdogFacade: ExpatsWatchdogFacade,
    private val messageSender: MessageSender,
) {
    private val log = KotlinLogging.logger { }

    @Scheduled(fixedDelayString = "\${watchdogs.check-delay}", initialDelay = 0, timeUnit = TimeUnit.MINUTES)
    fun processScheduled() {
        try {
            expatsWatchdogFacade.process()
        } catch (e: Exception) {
            log.error(e.message, e)
            messageSender.sendMessageToMe("Exception occurred :(\n${e.message}")
        }
    }

}
