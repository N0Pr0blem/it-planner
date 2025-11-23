package com.laba.it_planner.model.task

import com.laba.it_planner.model.project.Employee
import com.laba.it_planner.model.user.OauthUser
import jakarta.persistence.*

@Entity
@Table(name = "task_details")
class TaskDetails(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "from_user_id")
    val fromUser: Employee,

    @OneToOne
    @JoinColumn(name = "to_user_id")
    val toUser: Employee? = null,

    @Column(name = "description_file")
    val descriptionFile: String? = null
)