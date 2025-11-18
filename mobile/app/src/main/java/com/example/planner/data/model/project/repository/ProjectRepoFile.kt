package com.example.planner.data.model.project.repository

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
