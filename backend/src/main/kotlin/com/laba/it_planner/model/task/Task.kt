package com.laba.it_planner.model.task

import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.project.Project
import com.laba.it_planner.model.storage.Storage
import com.laba.it_planner.model.task.enum_old.TaskComplexity
import com.laba.it_planner.model.task.enum_old.TaskStatus
import com.laba.it_planner.model.task.enum_old.TaskUrgency
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "task")
class Task(
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: TaskStatus? = null,


    @Column(name = "creation_date")
    var creationDate: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "project_id")
    val project: Project,

    @OneToOne
    @JoinColumn(name = "storage_id", nullable = false)
    val storage: Storage? = null,

    @OneToOne
    @JoinColumn(name = "from_user_id")
    val fromUser: Employee,

    @OneToOne
    @JoinColumn(name = "to_user_id")
    var toUser: Employee? = null,
)