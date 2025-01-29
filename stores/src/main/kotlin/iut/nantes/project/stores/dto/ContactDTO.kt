package iut.nantes.project.stores.dto

import iut.nantes.project.stores.entity.ContactEntity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class ContactDTO(
    val id: Long, // Nullable pour la création
    @field:Email
    val email: String,

    @field:Pattern(regexp = "\\d{10}")
    val phone: String,

    val address: AddressDTO
) {
    fun toEntity() : ContactEntity = ContactEntity(id, email, phone, address.toEntity())
}
