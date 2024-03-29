package io.github.olxmute.watchdog.bot

import io.github.olxmute.watchdog.config.BotConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class RentApartmentsWatchdogBot(
    private val botConfig: BotConfig,
) : TelegramLongPollingBot(botConfig.token) {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getBotUsername() = botConfig.username

    override fun onUpdateReceived(update: Update) {
        logger.info("Bot received message from Chat ID: {}", update.message?.chatId)
    }

}