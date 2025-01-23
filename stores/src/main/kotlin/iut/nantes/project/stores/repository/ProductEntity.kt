package iut.nantes.project.stores.repository

import java.util.UUID

data class ProductEntity(
    val id: UUID,       // Identifiant unique du produit
    val name: String,   // Nom du produit
    val quantity: Int   // Quantité du produit
)
