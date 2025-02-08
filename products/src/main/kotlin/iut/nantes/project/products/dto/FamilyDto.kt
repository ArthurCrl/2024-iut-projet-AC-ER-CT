package iut.nantes.project.products.dto

data class FamilyRequest(
    val name: String,
    val description: String
)

data class FamilyResponse(
    val id: String,
    val name: String,
    val description: String
)