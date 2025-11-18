package com.example.planner.data.model.task

import com.example.planner.data.model.project.Project
import java.time.LocalDateTime


@Entity
@Table(name = "task_info")
class TaskInfo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "is_completed")
    var isCompleted: Boolean = false,

    @Column(name = "complexity")
    @Enumerated(EnumType.STRING)
    var complexity: TaskComplexity? = null,

    @Column(name = "urgency")
    @Enumerated(EnumType.STRING)
    var urgency: TaskUrgency? = null,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime? = null,

    @OneToOne
    @JoinColumn(name = "project_id")
    val project: Project,
)