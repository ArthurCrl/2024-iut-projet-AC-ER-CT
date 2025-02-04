package iut.nantes.project.products.repository

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductJpa, String> {
    fun existsByFamilyId(familyId: String): Boolean

    fun findByFamilyNameAndPriceBetween(
        familyName: String,
        minPrice: Double,
        maxPrice: Double
    ): List<ProductJpa>
}