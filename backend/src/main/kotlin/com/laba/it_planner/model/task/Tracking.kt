package com.laba.it_planner.model.task

import com.laba.it_planner.model.project.Employee
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "task_trekking")
class Tracking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "date")
    val date: LocalDate,

    @Column(name = "hours")
    val hours: Double,

    @ManyToOne
    @JoinColumn(name = "employee_id")
    val employee: Employee,

    @ManyToOne
    @JoinColumn(name = "task_details_id")
    val taskDetails: TaskDetails
)