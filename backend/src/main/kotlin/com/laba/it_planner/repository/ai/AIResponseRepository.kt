package com.laba.it_planner.repository.ai;

import com.laba.it_planner.dto.ai.AISavedResponseDto
import com.laba.it_planner.model.ai.AIResponse;
import com.laba.it_planner.repository.projection.AIResponseProjection
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AIResponseRepository : JpaRepository<AIResponse, Long> {
    @Query(
        """
        SELECT 
            ai_r.id              as responseId,
            ai.request           as request,
            ai_r.response        as response,
            ai.pattern           as pattern,
            ai_r.processing_time as processingTime,
            ai.send_date         as sendDate
    FROM ai_response ai_r
         join ai_request ai on ai.id = ai_r.request_id
         join user_info u on ai.author_id = u.id
    where u.email = :name
    """, nativeQuery = true
    )
    fun getAllByUsername(@Param("name") name: String): List<AIResponseProjection>
}
