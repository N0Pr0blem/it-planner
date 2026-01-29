package com.laba.it_planner.repository.storage

import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.storage.StorageFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StorageFileRepository: JpaRepository<StorageFile, Long> {
    fun findAllByStorage(storage: Storage):List<StorageFile>
    @Query("""
        select * from project_repository_file prf
        where prf.project_repository_id = :repoId
        and prf.name=:fileName
    """, nativeQuery = true)
    fun findByNameAndProjectRepoId(@Param("fileName") originalFilename: String?, @Param("repoId") projectRepoId: Long?): Optional<StorageFile>
}
