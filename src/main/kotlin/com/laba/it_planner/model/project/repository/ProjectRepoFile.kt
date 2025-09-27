package com.laba.it_planner.model.project.repository;

import jakarta.persistence.Column
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table;

@Entity
@Table(name = "project_repository_file")
class ProjectRepoFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_repository_id", nullable = false)
    var projectRepo: ProjectRepo,

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    var type: FileType,

    @Column(name = "name", columnDefinition = "TEXT")
    var name: String?
)
