package io.github.olxmute.watchdog.persistence.repository

import io.github.olxmute.watchdog.persistence.entity.ExpatsApartment
import org.springframework.data.jpa.repository.JpaRepository

interface ExpatsApartmentRepository : JpaRepository<ExpatsApartment, String> {
}