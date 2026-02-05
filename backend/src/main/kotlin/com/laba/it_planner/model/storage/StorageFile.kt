package com.laba.it_planner.model.storage

import com.laba.it_planner.utils.files.MimeType
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_id", nullable = false)
    var storage: Storage?,

    @Column(name = "mime_type", columnDefinition = "TEXT")
    @Enumerated(EnumType.STRING)
    var mimeType: MimeType
)