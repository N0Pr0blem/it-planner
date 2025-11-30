package com.laba.it_planner.model.task

import com.laba.it_planner.model.project.repository.FileType
import jakarta.persistence.*

@Entity
@Table(name = "task_file")
class TaskFile (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "task_id")
    var taskInfo: TaskInfo? = null,

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    var type: FileType,

    @Column(name = "name", columnDefinition = "TEXT")
    var name: String?
)