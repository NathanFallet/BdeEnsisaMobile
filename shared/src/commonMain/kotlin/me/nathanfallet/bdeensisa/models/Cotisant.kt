package me.nathanfallet.bdeensisa.models

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Cotisant(
    val userId: String,
    val expiration: LocalDate,
    val updatedAt: Instant?,
    val user: User? = null
)
