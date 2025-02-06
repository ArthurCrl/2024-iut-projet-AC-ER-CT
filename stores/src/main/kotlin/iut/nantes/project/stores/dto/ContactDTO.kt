package iut.nantes.project.stores.dto

import iut.nantes.project.stores.entity.AddressEntity
import iut.nantes.project.stores.entity.ContactEntity
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ContactDTO(
    val id: Long?,

    @field:Email
    val email: String,

    @field:Pattern(regexp = "\\d{10}")
    val phone: String,

    @field:Valid
    val address: AddressDTO
) {
    fun toEntity() : ContactEntity = ContactEntity(id, email, phone, address.toEntity())
}

data class AddressDTO(
    @field:Size(min = 5, max = 50)
    val street: String,

    @field:Size(min = 1, max = 30)
    val city: String,

    @field:Pattern(regexp = "\\d{5}")
    val postalCode: String
) {
    fun toEntity() : AddressEntity = AddressEntity(street, city, postalCode)
}