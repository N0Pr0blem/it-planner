package com.laba.it_planner.repository;

import com.laba.it_planner.model.project.repository.ProjectRepo
import com.laba.it_planner.model.project.repository.ProjectRepoFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProjectRepoFileRepository: JpaRepository<ProjectRepoFile, Long> {
    fun findAllByProjectRepo(projectRepo: ProjectRepo):List<ProjectRepoFile>
    @Query("""
        select * from project_repository_file prf
        where prf.project_repository_id = :repoId
        and prf.name=:fileName
    """, nativeQuery = true)
    fun findByNameAndProjectRepoId(@Param("fileName") originalFilename: String?, @Param("repoId") projectRepoId: Long?): Optional<ProjectRepoFile>
}
