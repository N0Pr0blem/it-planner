package com.laba.it_planner.model.storage

import jakarta.persistence.*

@Entity
@Table(name = "storage")
class Storage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "path")
    val path: String,
    @OneToMany(mappedBy = "storage")
    val files: List<StorageFile>
)