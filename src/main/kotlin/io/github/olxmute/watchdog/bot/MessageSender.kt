package io.github.olxmute.watchdog.bot

import io.github.olxmute.watchdog.config.BotConfig
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto
import java.util.concurrent.TimeUnit

@Component
class MessageSender(
    private val notifierBot: RentApartmentsWatchdogBot,
    private val botConfig: BotConfig
) {

    private var sentMessages = 0

    fun sendMessageToMe(message: String) {
        messageTemplate {
            val sendMessage = SendMessage(botConfig.chatId, message)
            notifierBot.execute(sendMessage)
        }
    }

    fun sendMediaGroupToMe(images: List<String>, message: String) {
        messageTemplate {
            val inputMediaPhotos = images.map { InputMediaPhoto(it) }
            inputMediaPhotos[0].parseMode = ParseMode.MARKDOWN
            inputMediaPhotos[0].caption = message
            notifierBot.execute(SendMediaGroup(botConfig.chatId, inputMediaPhotos))
        }
    }

    private fun messageTemplate(applyBlock: RentApartmentsWatchdogBot.() -> Any) {
        if (sentMessages >= 50) {
            TimeUnit.MINUTES.sleep(1)
            sentMessages = 0
        }

        when (val result = notifierBot.applyBlock()) {
            is List<*> -> sentMessages += result.size
            else -> sentMessages++
        }

        TimeUnit.SECONDS.sleep(2)
    }
}