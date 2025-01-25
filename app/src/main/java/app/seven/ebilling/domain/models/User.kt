package app.seven.ebilling.domain.models

import java.util.UUID


data class User(
    val id: UUID?,
    val name: String,
    val phoneNumber: String,
    val email: String?,
    val address: Address?
)
