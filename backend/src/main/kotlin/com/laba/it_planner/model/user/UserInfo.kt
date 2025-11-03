package com.laba.it_planner.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_info")
@PrimaryKeyJoinColumn(name = "id")
class UserInfo(
    @Column(name = "first_name")
    var firstName: String? = null,

    @Column(name = "second_name")
    var secondName: String? = null,

    @Column(name = "last_name")
    var lastName: String? = null,

    @Column(name = "email", unique = true)
    var email: String? = null,

    @Column(name = "registration_date")
    var registrationDate: LocalDateTime? = null,

    @Column(name = "profile_image")
    var profileImage: String? = null,
) : OauthUser()