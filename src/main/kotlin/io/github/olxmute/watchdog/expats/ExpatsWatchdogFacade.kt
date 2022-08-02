package io.github.olxmute.watchdog.expats

import io.github.olxmute.watchdog.bot.MessageSender
import io.github.olxmute.watchdog.config.WatchdogsConfig
import io.github.olxmute.watchdog.dto.ExpatsPropertyExtendedInfo
import io.github.olxmute.watchdog.persistence.entity.ExpatsApartment
import io.github.olxmute.watchdog.persistence.repository.ExpatsApartmentRepository
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.stereotype.Service

@Service
class ExpatsWatchdogFacade(
    private val expatsWebRepository: ExpatsWebRepository,
    private val messageSender: MessageSender,
    private val expatsApartmentRepository: ExpatsApartmentRepository,
    private val watchdogsConfig: WatchdogsConfig
) {
    private val log = KotlinLogging.logger { }

    fun process() {
        val foundExpatsProperties = expatsWebRepository.findAll().properties
            .map { it.copy(url = watchdogsConfig.expats.baseUrl + it.url) }

        val persistedExpatsPropertyItems = expatsApartmentRepository.findAll(
            PageRequest.of(0, foundExpatsProperties.size, Sort.by(Direction.DESC, "createdDate"))
        )

        val newApartments = foundExpatsProperties - persistedExpatsPropertyItems

        val appropriateApartments = newApartments
            .associateWith { expatsWebRepository.findExtendedInfoByUrl(it.url) }
            .filterValues { it.images.size > 2 }

        log.info { "Found new appropriate apartments in expats: ${appropriateApartments.size}" }

        appropriateApartments
            .forEach { (property, extendedInfo) ->
                val images = extendedInfo.images.take(10)
                val messageText = buildMessage(property, extendedInfo)

                try {
                    messageSender.sendMediaGroupToMe(images, messageText)
                } catch (e: Exception) {
                    log.error(e.message, e)
                    messageSender.sendMessageToMe("Exception occurred on apartment: ${property.url}\n(\n${e.message}")
                }

                expatsApartmentRepository.save(property)
            }

    }

    private fun buildMessage(
        property: ExpatsApartment,
        propertyExtendedInfo: ExpatsPropertyExtendedInfo
    ): String {
        val stringBuilder = StringBuilder()
            .appendLine("*${property.name}*").appendLine()
            .appendLine("Price: ${property.priceText}").appendLine()
            .appendLine("Location: ${property.location}").appendLine()

        with(propertyExtendedInfo) {
            with(stringBuilder) {
                floor?.let { appendLine("Floor: $it") }
                usableArea?.let { appendLine("Usable area: $it") }
                gardenArea?.let { appendLine("Garden area: $it") }
                terraceArea?.let { appendLine("Terrace area: $it") }
                cellarArea?.let { appendLine("Cellar area: $it") }
                moveInDate?.let { appendLine("Move-in date: $it") }
            }
        }
        stringBuilder.appendLine().appendLine(property.url)

        return stringBuilder.toString()
    }

}