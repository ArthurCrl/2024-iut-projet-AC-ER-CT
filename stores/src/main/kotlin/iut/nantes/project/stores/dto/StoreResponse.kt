package iut.nantes.project.stores.dto

import iut.nantes.project.stores.entity.ContactEntity

data class StoreResponse(
    val id: Long,
    val name: String,
    val contact: ContactEntity,
    val products: List<ProductResponse>
)

data class ProductResponse(
    val id: String,
    val name: String, // Récupéré via WebClient
    val quantity: Int
)
