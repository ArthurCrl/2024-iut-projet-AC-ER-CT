package iut.nantes.project.stores.dto

import iut.nantes.project.stores.entity.ContactEntity
import iut.nantes.project.stores.entity.ProductEntity
import iut.nantes.project.stores.entity.StoreEntity
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class StoreDTO(
    val id: Long,
    @field:NotBlank
    @field:Size(min = 3, max = 30)
    val name: String,

    @field:Valid
    val contact: ContactDTO,

    val products: MutableList<ProductDTO>? = mutableListOf() // Ignoré à la création
) {
    fun toEntity(contactEntity : ContactEntity): StoreEntity = StoreEntity(id, name, contact = contactEntity)
}


data class ProductDTO(
    val id: UUID,
    val name: String,
    val quantity: Int
) {
    fun toEntity(): ProductEntity = ProductEntity(id = id, name = name, quantity = quantity)
}