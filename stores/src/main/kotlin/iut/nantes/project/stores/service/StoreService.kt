package iut.nantes.project.stores.service

import iut.nantes.project.stores.service.ProductServiceClient
import iut.nantes.project.stores.repository.StoreEntity
import iut.nantes.project.stores.repository.StoreRepository
import org.springframework.stereotype.Service

@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val productServiceClient: ProductServiceClient // Injection du client ProductService
) {

    // Créer un magasin
    suspend fun createStore(store: StoreEntity): StoreEntity {
        // Vérifier si le contact existe ou le créer (à implémenter selon tes besoins)

        // Appel au service Product pour récupérer les produits associés au magasin
        val products = productServiceClient.getProductsForStore(store.id ?: 0)

        // Associer les produits récupérés au magasin
        store.products = products

        // Sauvegarder le magasin dans la base de données
        return storeRepository.save(store)
    }

    // Récupérer un magasin par ID
    suspend fun getStoreById(id: Long): StoreEntity {
        val store = storeRepository.findById(id).orElseThrow {
            throw RuntimeException("Store not found")
        }

        // Appel au service Product pour récupérer les produits associés
        val products = productServiceClient.getProductsForStore(id)

        // Associer les produits au magasin
        store.products = products
        return store
    }

    // Récupérer tous les magasins triés par nom
    fun getAllStores(): List<StoreEntity> {
        return storeRepository.findAll().sortedBy { it.name }
    }

    // Mettre à jour un magasin (sans toucher à la liste de produits)
    fun updateStore(id: Long, store: StoreEntity): StoreEntity {
        val existingStore = storeRepository.findById(id).orElseThrow {
            throw RuntimeException("Store not found")
        }

        // Mise à jour du magasin (pas de modification des produits)
        existingStore.name = store.name
        existingStore.contact = store.contact

        return storeRepository.save(existingStore)
    }

    // Supprimer un magasin
    fun deleteStore(id: Long) {
        val store = storeRepository.findById(id).orElseThrow {
            throw RuntimeException("Store not found")
        }

        // Suppression du magasin
        storeRepository.delete(store)
    }
}
