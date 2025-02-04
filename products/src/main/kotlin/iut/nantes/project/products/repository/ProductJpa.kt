package iut.nantes.project.products.repository

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "products")
data class ProductJpa(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, length = 20)
    val name: String,

    @Column(nullable = true, length = 100)
    val description: String? = null,

    @Embedded
    val price: PriceJpa,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "family_id", nullable = false)
    val family: FamilyJpa
) {
    constructor() : this("", "", null, PriceJpa(), FamilyJpa())
}

@Embeddable
data class PriceJpa(
    @Column(nullable = false)
    val amount: Double = 0.0,

    @Column(nullable = false, length = 3)
    val currency: String = "EUR"
) {
    constructor() : this(0.0, "")
}