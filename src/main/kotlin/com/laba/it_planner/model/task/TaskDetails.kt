package com.laba.it_planner.model.task

import com.laba.it_planner.model.user.OauthUser
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "task_details")
class TaskDetails (user: OauthUser) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToOne
    @JoinColumn(name = "from_user_id")
    val fromUser: OauthUser = user

    @OneToOne
    @JoinColumn(name = "to_user_id")
    val toUser: OauthUser? = null

    @Column(name = "description_file")
    val descriptionFile: String? = null
}