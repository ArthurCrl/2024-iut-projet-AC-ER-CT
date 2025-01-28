package iut.nantes.project.stores.dto

data class ContactDto(
    val id: Long?,
    val email: String,
    val phone: String,
    val address: AddressDto
)
