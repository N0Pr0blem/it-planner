package com.laba.it_planner.model.storage

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "storage_file")
class StorageFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name", columnDefinition = "TEXT")
    var name: String?,

    @Column(name = "creation_date")
    var creationDate: LocalDateTime?,

    @ManyToOne
    @JoinColumn(name = "storage_id", nullable = false)
    var storage: Storage?,
)