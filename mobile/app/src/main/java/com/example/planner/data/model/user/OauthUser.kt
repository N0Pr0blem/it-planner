package com.example.planner.data.model.user


@Entity
@Table(name = "oauth_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Proxy(lazy = false)
open class OauthUser (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, name = "username")
    var username: String? = null,

    @Column(name = "password")
    var password: String? = null,

    @Column(name = "enabled")
    var enabled: Boolean = false,

    @Column(name = "verification_code")
    var verificationCode: String? = null,

    @Column(name = "oauth_role")
    @Enumerated(EnumType.STRING)
    var role: OauthRole = OauthRole.USER
)