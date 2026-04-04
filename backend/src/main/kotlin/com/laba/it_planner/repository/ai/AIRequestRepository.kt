package com.laba.it_planner.repository.ai

import com.laba.it_planner.model.ai.AIRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.security.Principal
import java.time.LocalDate

interface AIRequestRepository: JpaRepository<AIRequest, Long> {
    @Query("""
        SELECT ai.*
FROM user_info u
         join ai_request ai on ai.author_id = u.id
where u.email = :name
  AND ai.send_date > :dateFrom
  AND ai.send_date < :dateTo
    """, nativeQuery = true)
    fun getAllUsersRequestsByDate(@Param("name") name: String,
                                  @Param("dateFrom") dateFrom: LocalDate,
                                  @Param("dateTo") dateTo: LocalDate): List<AIRequest>
}