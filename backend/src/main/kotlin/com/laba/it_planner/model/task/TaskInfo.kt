package com.laba.it_planner.model.task

import com.laba.it_planner.model.project.Project
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
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