package com.example.planner.data.model.project.repository

import com.example.planner.data.model.project.Project


@Entity
@Table(name = "project_repository")
class ProjectRepo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "project_id")
    val project: Project,

    @Column(name = "path")
    val path: String
)