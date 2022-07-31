package io.github.olxmute.watchdog.bot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class MessageSender(
    private val notifierBot: RentApartmentsWatchdogBOt
) {

    fun sendMessageToMe(message: String) {
        notifierBot.execute(SendMessage("438363595", message))
    }

}