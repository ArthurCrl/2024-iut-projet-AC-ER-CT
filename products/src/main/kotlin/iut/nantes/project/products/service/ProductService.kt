package iut.nantes.project.products.service

import iut.nantes.project.products.controlleur.dto.ProductRequest
import iut.nantes.project.products.controlleur.dto.ProductResponse
import iut.nantes.project.products.controlleur.dto.PriceResponse
import iut.nantes.project.products.controlleur.dto.FamilyResponse
import iut.nantes.project.products.repository.FamilyRepository
import iut.nantes.project.products.repository.PriceJpa
import iut.nantes.project.products.repository.ProductJpa
import iut.nantes.project.products.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository,
    private val familyRepository: FamilyRepository
) {
    private fun validateProductRequest(request: ProductRequest) {
        if (request.name.length !in 2..20) {
            throw IllegalArgumentException("Product name must be between 2 and 20 characters")
        }

        request.description?.let {
            if (it.length !in 5..100) {
                throw IllegalArgumentException("Product description must be between 5 and 100 characters")
            }
        }

        if (request.price.amount <= 0 || !request.price.currency.matches(Regex("^[A-Z]{3}\$"))) {
            throw IllegalArgumentException("Invalid price amount or currency format")
        }
    }

    private fun toProductResponse(product: ProductJpa): ProductResponse {
        return ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            price = PriceResponse(
                amount = product.price.amount,
                currency = product.price.currency
            ),
            family = FamilyResponse(
                id = product.family.id,
                name = product.family.name,
                description = product.family.description
            )
        )
    }

    fun createProduct(request: ProductRequest): ProductResponse {
        // Validation des champs du produit
        validateProductRequest(request)

        // Vérification de la famille
        val family = familyRepository.findById(request.familyId)
            .orElseThrow { IllegalArgumentException("Family not found with given ID") }

        // Création et sauvegarde du produit
        val product = ProductJpa(
            name = request.name,
            description = request.description,
            price = PriceJpa(
                amount = request.price.amount,
                currency = request.price.currency
            ),
            family = family
        )
        productRepository.save(product)

        return toProductResponse(product)
    }

    fun getProducts(familyName: String?, minPrice: Double?, maxPrice: Double?): List<ProductResponse> {
        if ((minPrice != null && maxPrice != null) && (minPrice <= 0 || minPrice >= maxPrice)) {
            throw IllegalArgumentException("Invalid price range: 0 < minprice < maxprice")
        }

        val products = productRepository.findAll().filter { product ->
            (familyName == null || product.family.name == familyName) &&
                    (minPrice == null || product.price.amount >= minPrice) &&
                    (maxPrice == null || product.price.amount <= maxPrice)
        }

        return products.map { toProductResponse(it) }
    }

    private fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getProductById(id: String): ProductResponse {
        if (!isValidUUID(id)) {
            throw IllegalArgumentException("Invalid UUID format")
        }

        val product = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("No product found with the given ID") }

        return toProductResponse(product)
    }

    fun updateProduct(id: String, request: ProductRequest): ProductResponse {
        // Validation de l'UUID
        if (!isValidUUID(id)) {
            throw IllegalArgumentException("Invalid UUID format")
        }

        // Recherche du produit existant
        val existingProduct = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("No product found with the given ID") }

        // Validation des champs du produit
        if (request.name.length !in 2..20 || (request.description?.length ?: 0) !in 5..100) {
            throw IllegalArgumentException("Invalid product name or description length")
        }

        if (request.price.amount <= 0 || !request.price.currency.matches(Regex("^[A-Z]{3}\$"))) {
            throw IllegalArgumentException("Invalid price amount or currency format")
        }

        // Vérification de l'existence de la famille
        val family = familyRepository.findById(request.familyId)
            .orElseThrow { IllegalArgumentException("Family not found with given ID") }

        // Mise à jour du produit
        val updatedProduct = existingProduct.copy(
            name = request.name,
            description = request.description,
            price = PriceJpa(request.price.amount, request.price.currency),
            family = family
        )

        productRepository.save(updatedProduct)

        return toProductResponse(updatedProduct)
    }

}
