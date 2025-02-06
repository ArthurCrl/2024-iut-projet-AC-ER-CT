package iut.nantes.project.stores.entity

import iut.nantes.project.stores.dto.ProductDTO
import iut.nantes.project.stores.dto.StoreDTO
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "stores")
data class StoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false, length = 30)
    var name: String,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "contact_id", nullable = false)
    val contact: ContactEntity,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "store_id")
    var products: MutableList<ProductEntity> = mutableListOf()
) {
    fun toDto() = StoreDTO(id, name, contact.toDto(), products.map { it.toDto() }.toMutableList())
}

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    val id: UUID, // UUID

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    var quantity: Int,
) {
    fun toDto() = ProductDTO(id, name, quantity)
}
