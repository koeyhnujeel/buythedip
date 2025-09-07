package com.zunza.buythedip.crypto.entity

import com.zunza.buythedip.common.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class Crypto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 20)
    val name: String,

    @Column(nullable = false, length = 20)
    val symbol: String,

    @Column
    val logo: String? = null,

    @OneToOne(
        mappedBy = "crypto",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    val metadata: CryptoMetadata? = null
) : BaseEntity()
