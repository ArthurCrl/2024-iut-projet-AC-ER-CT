package iut.nantes.project.stores.service

import iut.nantes.project.stores.dto.ProductDTO
import iut.nantes.project.stores.dto.StoreDTO
import iut.nantes.project.stores.entity.ProductEntity
import iut.nantes.project.stores.repository.ContactRepository
import iut.nantes.project.stores.repository.StoreRepository
import iut.nantes.project.stores.entity.StoreEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import iut.nantes.project.stores.config.WebClientConfig
import org.springframework.web.reactive.function.client.WebClient
import java.util.*
import kotlin.NoSuchElementException

@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactRepository: ContactRepository,
    private val webClient: WebClient
) {
    fun getAllStores(): List<StoreDTO> {
        return storeRepository.findAll().sortedBy { it.name }.map { it.toDto() }
    }

    fun getStoreById(id: Long): StoreDTO {
        return storeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Magasin non trouvé pour l'id $id") }
            .toDto()
    }

    @Transactional
    fun createStore(storeDTO: StoreDTO): StoreDTO {
        // Vérification du nom du magasin
        if (storeDTO.name.length !in 3..30) {
            throw IllegalArgumentException("Le nom du magasin doit contenir entre 3 et 30 caractères.")
        }

        val contact = storeDTO.contact.id.let { contactRepository.findById(it).orElse(null) }
            ?: storeDTO.contact.toEntity()  // Si l'id est null, on crée un nouveau contact

        val storeEntity = storeDTO.toEntity(contact)

        // Sauvegarde du magasin
        val savedStore = storeRepository.save(storeEntity)

        return savedStore.toDto()
    }


    @Transactional
    fun updateStore(id: Long, storeDTO: StoreDTO): StoreDTO {
        if (storeDTO.name.length !in 3..30) {
            throw IllegalArgumentException("Le nom du magasin doit contenir entre 3 et 30 caractères.")
        }

        val existingStore = storeRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Magasin non trouvé pour l'id $id") }

        val updatedStore = existingStore.copy(
            name = storeDTO.name,
            contact = storeDTO.contact.toEntity()
        )

        return storeRepository.save(updatedStore).toDto()
    }

    fun deleteStore(id: Long) {
        if (!storeRepository.existsById(id)) {
            throw IllegalArgumentException("Magasin non trouvé pour l'id $id")
        }
        storeRepository.deleteById(id)
    }

    fun addProductToStock(storeId: Long, productId: UUID, quantity: Int?): ProductDTO {
        val store = storeRepository.findById(storeId)
            .orElseThrow { IllegalArgumentException("Le magasin avec l'ID $storeId n'existe pas.") }

        val validQuantity = quantity?.takeIf { it > 0 } ?: 1

        val product = getProductByIdFromProductService(productId)

        var exProduct = store.products.find { it.id == productId }
        if (exProduct != null) {
            exProduct.quantity += validQuantity
        } else {
            exProduct = ProductEntity(product.id, product.name, validQuantity)
            store.products.add(exProduct)
        }

        return exProduct.toDto()
    }

    private fun getProductByIdFromProductService(productId: UUID): ProductDTO {
        // Effectuer un appel HTTP avec WebClient pour récupérer le produit
        return webClient.get()
            .uri("/api/v1/products/{productId}", productId)
            .retrieve()
            .bodyToMono(ProductDTO::class.java)
            .block() // Blocage jusqu'à la récupération de la réponse
            ?: throw IllegalArgumentException("Produit avec l'ID $productId non trouvé dans le service product.")
    }

    fun removeProductFromStock(storeId: Long, productId: UUID, quantity: Int): ProductDTO {
        if (quantity <= 0) throw IllegalArgumentException("La quantité doit être positive.")

        val store = storeRepository.findById(storeId).orElseThrow {
            throw NoSuchElementException("Magasin non trouvé avec l'ID : $storeId")
        }

        val product = store.products.find { it.id == productId }
            ?: throw NoSuchElementException("Produit non trouvé dans le magasin.")

        if (product.quantity - quantity < 0) {
            throw IllegalStateException("Impossible de retirer plus de produits qu'il n'y en a en stock.")
        }

        product.quantity -= quantity
        storeRepository.save(store)

        return product.toDto()
    }

    fun deleteProductsFromStock(storeId: Long, productIds: List<UUID>) {
        if (productIds.distinct().size != productIds.size) {
            throw IllegalArgumentException("La liste de produits contient des doublons.")
        }

        val store = storeRepository.findById(storeId).orElseThrow {
            throw NoSuchElementException("Magasin non trouvé avec l'ID : $storeId")
        }

        store.products.removeAll { it.id in productIds }
        storeRepository.save(store)
    }
}

