package com.laba.it_planner.model.project

import com.laba.it_planner.model.user.ProjectRole
import com.laba.it_planner.model.user.UserInfo
import jakarta.persistence.*

@Entity
@Table(name = "employee")
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "project_id")
    val project: Project,

    @Enumerated(EnumType.STRING)
    @Column(name = "project_role")
    var projectRole: ProjectRole,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserInfo

)