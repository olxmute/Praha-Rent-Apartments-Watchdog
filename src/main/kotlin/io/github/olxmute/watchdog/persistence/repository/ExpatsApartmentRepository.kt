package io.github.olxmute.watchdog.persistence.repository

import io.github.olxmute.watchdog.persistence.entity.ExpatsApartment
import org.springframework.data.jpa.repository.JpaRepository

interface ExpatsApartmentRepository : JpaRepository<ExpatsApartment, String> {

//    @Query(
//        """
//            FROM ExpatsApartment epi
//            WHERE epi.createdDate = (
//                SELECT MAX(epi2.createdDate)
//                FROM ExpatsApartment epi2
//            )
//        """
//    )
//    fun findLatest(): List<ExpatsApartment>

}