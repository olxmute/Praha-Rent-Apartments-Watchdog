package io.github.olxmute.watchdog.persistence.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id

@Entity
@EntityListeners(AuditingEntityListener::class)
data class ExpatsApartment(
    @Id
    val id: String,
    val name: String,
    @JsonProperty("price_text")
    val priceText: String,
    val url: String
) {
    @LastModifiedDate
    lateinit var createdDate: Instant
        private set
}