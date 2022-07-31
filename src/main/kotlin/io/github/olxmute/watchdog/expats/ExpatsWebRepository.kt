package io.github.olxmute.watchdog.expats

import io.github.olxmute.watchdog.config.WatchdogsConfig
import io.github.olxmute.watchdog.dto.ExpatsSearchResponseDto
import org.jsoup.Jsoup
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

@Repository
class ExpatsWebRepository(
    private val restTemplate: RestTemplate,
    private val watchdogsConfig: WatchdogsConfig
) {

    fun findAll(): ExpatsSearchResponseDto {
        val fullSearchUrl = watchdogsConfig.expats.baseUrl + watchdogsConfig.expats.searchUrl
        return restTemplate.postForObject(fullSearchUrl)
            ?: throw RuntimeException("Exception during fetching data from expats.cz")
    }

    fun findExtendedInfoByUrl(url: String): ExpatsPropertyExtendedInfo {
        val document = Jsoup.connect(url).get()
        val images = document.select(".gallery ul li a")
            .map { watchdogsConfig.expats.baseUrl + it.attr("href") }

        val description = document.select(".description").text()

        val extendedInfo = document.select(".attributes table tr").associate {
            it.child(0).text() to it.child(1).text()
        }

        val floor = extendedInfo["Floor"]
        val usableArea = extendedInfo["Usable area"]
        val gardenArea = extendedInfo["Garden area"]
        val terraceArea = extendedInfo["Terrace area"]
        val cellarArea = extendedInfo["Cellar area"]
        val moveInDate = extendedInfo["Move-in date"]

        return ExpatsPropertyExtendedInfo(
            images = images,
            description = description,
            floor = floor,
            usableArea = usableArea,
            gardenArea = gardenArea,
            terraceArea = terraceArea,
            cellarArea = cellarArea,
            moveInDate = moveInDate,
        )
    }

}

data class ExpatsPropertyExtendedInfo(
    val images: List<String>,
    val description: String?,
    val floor: String?,
    val usableArea: String?,
    val gardenArea: String?,
    val terraceArea: String?,
    val cellarArea: String?,
    val moveInDate: String?,
)