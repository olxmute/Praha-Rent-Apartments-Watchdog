package io.github.olxmute.watchdog.bot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class RentApartmentsWatchdogBOt(

    @Value("\${bot.username}")
    private val botUsername: String,

    @Value("\${bot.token}")
    private val botToken: String

) : TelegramLongPollingBot() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getBotToken() = botToken

    override fun getBotUsername() = botUsername

    override fun onUpdateReceived(update: Update) {
        logger.info("Message sent to bot. Chat ID: {}", update.message.chatId)
    }

}