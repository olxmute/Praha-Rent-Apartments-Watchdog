package io.github.olxmute.watchdog.expats

import io.github.olxmute.watchdog.config.WatchdogsConfig
import io.github.olxmute.watchdog.dto.ExpatsPropertyExtendedInfo
import io.github.olxmute.watchdog.persistence.entity.ExpatsApartment
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Repository

@Repository
class ExpatsWebRepository(
    private val watchdogsConfig: WatchdogsConfig
) {

    fun findAll(): List<ExpatsApartment> {
        val fullSearchUrl = watchdogsConfig.expats.baseUrl + watchdogsConfig.expats.searchUrl

        val apartments = Jsoup.connect(fullSearchUrl)
            .get()
            .select(".property-info .list")[0]
            .children() as Iterable<Element>

        return apartments.filter { it.attributes().isEmpty }
            .reversed()
            .map {
                val url = it.select("h2 a").attr("href")
                ExpatsApartment(
                    id = url.split("/")[4].split("-")[0],
                    name = it.select("h2").text(),
                    location = it.select("h3").text(),
                    priceText = it.select("strong").text(),
                    url = watchdogsConfig.expats.baseUrl + url
                )
            }
    }

    fun findExtendedInfoByUrl(url: String): ExpatsPropertyExtendedInfo {
        val document = Jsoup.connect(url).get()
        val images = document.select(".gallery ul li a")
            .map { watchdogsConfig.expats.baseUrl + it.attr("href") }

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
            floor = floor,
            usableArea = usableArea,
            gardenArea = gardenArea,
            terraceArea = terraceArea,
            cellarArea = cellarArea,
            moveInDate = moveInDate,
        )
    }

}
