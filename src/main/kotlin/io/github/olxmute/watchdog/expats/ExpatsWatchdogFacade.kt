package io.github.olxmute.watchdog.expats

import io.github.olxmute.watchdog.bot.MessageSender
import io.github.olxmute.watchdog.dto.ExpatsApartmentExtendedInfo
import io.github.olxmute.watchdog.persistence.entity.ExpatsApartment
import io.github.olxmute.watchdog.persistence.repository.ExpatsApartmentRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ExpatsWatchdogFacade(
    private val expatsWebRepository: ExpatsWebRepository,
    private val messageSender: MessageSender,
    private val expatsApartmentRepository: ExpatsApartmentRepository
) {
    private val log = KotlinLogging.logger { }

    fun process() {
        log.info { "Looking for new apartments in expats..." }
        val newApartments = findNewApartments()

        val appropriateApartments = newApartments
            .filterNot { it.priceText.contains("EUR") && it.priceText.toNumber() > 1300 }
            .associateWith { expatsWebRepository.findExtendedInfoByUrl(it.url) }
            .filterValues { it.images.size > 2 }

        log.info { "Found new appropriate apartments in expats: ${appropriateApartments.size}" }

        appropriateApartments
            .forEach { (apartment, extendedInfo) ->
                val images = extendedInfo.images.take(10)
                val messageText = buildMessage(apartment, extendedInfo)

                try {
                    messageSender.sendMediaGroupToMe(images, messageText)
                    log.info { "Sent apartment: ${apartment.url}" }
                } catch (e: Exception) {
                    log.error(e.message, e)
                    messageSender.sendMessageToMe("Exception occurred on apartment: ${apartment.url}\n(\n${e.message}")
                }

                expatsApartmentRepository.save(apartment)
            }

        if (appropriateApartments.isNotEmpty()) {
            log.info { "Finished processing last apartments" }
        }

    }

    private fun findNewApartments(): List<ExpatsApartment> {
        val newExpatsAdvertisements = expatsWebRepository.findAll()
        val newAdvertisementIds = newExpatsAdvertisements.map { it.id }
        val alreadyProcessedApartments = expatsApartmentRepository.findAllById(newAdvertisementIds)

        return newExpatsAdvertisements - alreadyProcessedApartments.toSet()
    }

    private fun buildMessage(
        apartment: ExpatsApartment,
        apartmentExtendedInfo: ExpatsApartmentExtendedInfo
    ): String {
        val stringBuilder = StringBuilder()
            .appendLine("*${apartment.name}*").appendLine()
            .appendLine("*Price:* ${apartment.priceText}")
            .appendLine("*Location:* ${apartment.location}").appendLine()

        with(apartmentExtendedInfo) {
            with(stringBuilder) {
                floor?.let { appendLine("*Floor:* $it") }
                usableArea?.let { appendLine("*Usable area:* $it") }
                gardenArea?.let { appendLine("*Garden area:* $it") }
                terraceArea?.let { appendLine("*Terrace area:* $it") }
                cellarArea?.let { appendLine("*Cellar area:* $it") }
                moveInDate?.let { appendLine("*Move-in date:* $it") }
            }
        }
        stringBuilder.appendLine().appendLine(apartment.url)

        return stringBuilder.toString()
    }

    private fun String.toNumber() = this.filter { it.isDigit() }.toIntOrNull() ?: 0

}