package io.github.olxmute.watchdog.dto

import io.github.olxmute.watchdog.persistence.entity.ExpatsApartment

data class ExpatsSearchResponseDto(
    val properties: List<ExpatsApartment>,
)
