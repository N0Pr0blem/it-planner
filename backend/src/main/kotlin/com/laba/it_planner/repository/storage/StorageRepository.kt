package com.laba.it_planner.repository.storage

import com.laba.it_planner.model.storage.Storage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StorageRepository: JpaRepository<Storage, Long> {
    @Query("""
        select pr.* from project_repository pr
        join project p on pr.project_id = p.id
        join employee e on p.id = e.project_id
        join oauth_user u on e.user_id = u.id
        where p.id = :projectId
        and u.username = :username
    """, nativeQuery = true)
    fun findByProjectIdAndUsername(@Param("projectId") projectId: Long,@Param("username") username:String): Optional<Storage>
}