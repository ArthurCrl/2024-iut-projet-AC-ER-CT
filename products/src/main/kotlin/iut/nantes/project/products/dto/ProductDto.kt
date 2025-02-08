package iut.nantes.project.products.dto

import iut.nantes.project.products.repository.ProductJpa

data class ProductRequest(
    val name: String,
    val description: String?,
    val price: PriceRequest,
    val familyId: String
)

data class PriceRequest(
    val amount: Double,
    val currency: String
)

data class ProductResponse(
    val id: String,
    val name: String,
    val description: String?,
    val price: PriceResponse,
    val family: FamilyResponse
)

data class PriceResponse(
    val amount: Double,
    val currency: String
)