package com.laba.it_planner.model.project

import com.laba.it_planner.model.user.OauthUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "project")
class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user", nullable = false)
    var createdUser: OauthUser? = null
)