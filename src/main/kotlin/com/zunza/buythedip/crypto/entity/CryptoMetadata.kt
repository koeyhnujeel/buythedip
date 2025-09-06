package com.zunza.buythedip.crypto.entity

import com.zunza.buythedip.common.BaseEntity
import com.zunza.buythedip.crypto.converter.StringListConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.OneToOne

@Entity
class CryptoMetadata(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crypto_id", nullable = false)
    val crypto: Crypto,

    @Lob
    @Column(columnDefinition = "TEXT")
    val description: String,

    @Column(columnDefinition = "TEXT")
    @Convert(converter = StringListConverter::class)
    val website: List<String> = listOf(),

    @Column(columnDefinition = "TEXT")
    @Convert(converter = StringListConverter::class)
    val twitter: List<String> = listOf(),

    @Column(columnDefinition = "TEXT")
    @Convert(converter = StringListConverter::class)
    val explorer: List<String> = listOf(),

    @Column(columnDefinition = "TEXT")
    @Convert(converter = StringListConverter::class)
    val tagNames: List<String> = listOf()
) : BaseEntity()
