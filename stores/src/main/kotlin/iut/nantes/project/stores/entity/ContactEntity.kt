package iut.nantes.project.stores.entity

import iut.nantes.project.stores.dto.AddressDTO
import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
@Table(name = "contacts")
data class ContactEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:Email
    @field:NotBlank
    val email: String,

    @field:Pattern(regexp = "\\d{10}", message = "Phone number must contain 10 digits")
    @field:NotBlank
    val phone: String,

    @Embedded
    val address: AddressEntity
) {
    fun toDto() : ContactDto = ContactDto(id, email, phone, address.toDto())
}


@Embeddable
data class AddressEntity(
    @field:NotBlank
    @field:Size(min = 5, max = 50)
    val street: String,

    @field:NotBlank
    @field:Size(min = 1, max = 30)
    val city: String,

    @field:Pattern(regexp = "\\d{5}", message = "Postal code must contain exactly 5 digits")
    val postalCode: String
){
    fun toDto() : AddressDTO = AddressDTO(street, city, postalCode)
}
