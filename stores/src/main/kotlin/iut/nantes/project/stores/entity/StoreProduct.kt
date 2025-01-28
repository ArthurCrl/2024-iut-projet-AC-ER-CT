package iut.nantes.project.stores.entity

import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Embeddable
data class StoreProduct(
    @NotBlank(message = "L'ID du produit est obligatoire.")
    val productId: String,

    @Min(value = 1, message = "La quantité doit être au moins de 1.")
    val quantity: Int
)
