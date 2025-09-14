package com.laba.it_planner.model.project

import com.laba.it_planner.model.user.OauthUser
import com.laba.it_planner.model.user.ProjectRole
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
    var projectRole: ProjectRole? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: OauthUser? = null

)