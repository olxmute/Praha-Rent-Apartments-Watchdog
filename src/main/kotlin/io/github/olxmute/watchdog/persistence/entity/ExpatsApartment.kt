package io.github.olxmute.watchdog.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import java.time.Instant
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
data class ExpatsApartment(
    @Id
    val id: String,
    val name: String,
    val priceText: String,
    val location: String,
    val url: String
) {
    @LastModifiedDate
    lateinit var createdDate: Instant
        private set
}