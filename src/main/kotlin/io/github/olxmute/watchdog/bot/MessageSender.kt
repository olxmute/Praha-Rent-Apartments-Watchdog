package io.github.olxmute.watchdog.bot

import io.github.olxmute.watchdog.config.BotConfig
import mu.KotlinLogging
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.support.RetryTemplateBuilder
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException

@Component
class MessageSender(
    private val notifierBot: RentApartmentsWatchdogBot,
    private val botConfig: BotConfig
) {

    private val log = KotlinLogging.logger { }

    private val retryTemplate = RetryTemplateBuilder()
        .maxAttempts(3)
        .customBackoff(ExponentialBackOffPolicy().apply {
            initialInterval = 2 * 60 * 1000 // 2 minutes
            multiplier = 2.0
        })
        .retryOn(TelegramApiRequestException::class.java)
        .withListener(object : RetryListener {
            override fun <T : Any?, E : Throwable?> onError(
                context: RetryContext?, callback: RetryCallback<T, E>?, throwable: Throwable?
            ) {
                log.warn(throwable) { "Error occurred while sending message" }
            }
        })
        .build()

    fun sendMessageToMe(message: String) {
        val sendMessage = SendMessage(botConfig.chatId, message)
        retryTemplate.execute<Any, TelegramApiRequestException> {
            notifierBot.execute(sendMessage)
        }
    }

    fun sendMediaGroupToMe(images: List<String>, message: String) {
        val inputMediaPhotos = images.map(::InputMediaPhoto)
        inputMediaPhotos[0].parseMode = ParseMode.MARKDOWN
        inputMediaPhotos[0].caption = message
        retryTemplate.execute<List<Message>, TelegramApiRequestException> {
            notifierBot.execute(
                SendMediaGroup(
                    botConfig.chatId,
                    inputMediaPhotos
                )
            )
        }
    }
}