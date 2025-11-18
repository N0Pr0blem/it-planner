package com.example.planner.data.model.task


import com.example.planner.data.model.user.OauthUser

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