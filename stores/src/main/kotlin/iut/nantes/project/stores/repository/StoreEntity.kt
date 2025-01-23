package iut.nantes.project.stores.repository

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "stores")
data class StoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotBlank(message = "Le nom du magasin est obligatoire.")
    var name: String,

    @ManyToOne(cascade = [CascadeType.ALL])
    var contact: ContactEntity, // Le magasin doit avoir un contact

    @OneToMany
    var products: List<ProductEntity> = emptyList() // Liste des produits du magasin, vide par défaut
)
