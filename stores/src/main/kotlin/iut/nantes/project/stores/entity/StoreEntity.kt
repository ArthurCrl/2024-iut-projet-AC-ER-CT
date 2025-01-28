package iut.nantes.project.stores.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "stores")
data class StoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @NotBlank(message = "Le nom du magasin est obligatoire.")
    @Size(min = 3, max = 30, message = "Le nom doit faire entre 3 et 30 caractères.")
    @Column(unique = true)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    val contact: ContactEntity,

    @ElementCollection
    @CollectionTable(name = "store_products", joinColumns = [JoinColumn(name = "store_id")])
    val products: List<StoreProduct> = emptyList() // Stocke ID + quantité, mais pas le nom
)

