package com.laba.it_planner.model.project.repository

import com.laba.it_planner.model.project.Project
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "project_repository")
class ProjectRepo(createdProject: Project) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToOne
    @JoinColumn(name = "project_id")
    val project: Project = createdProject

    @Column(name = "path")
    val path: String? = createdProject.createdUser?.username+"/"+createdProject.name+"/"
}