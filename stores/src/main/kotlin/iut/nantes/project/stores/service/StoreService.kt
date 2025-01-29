package iut.nantes.project.stores.service

import com.sun.media.sound.InvalidDataException
import iut.nantes.project.stores.entity.StoreEntity
import iut.nantes.project.stores.repository.StoreRepository
import iut.nantes.project.stores.repository.ContactRepository
import iut.nantes.project.stores.dto.StoreResponse
import iut.nantes.project.stores.exception.StoreNotFoundException
import iut.nantes.project.stores.exception.InvalidDataException
import iut.nantes.project.stores.webclient.ProductWebClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactRepository: ContactRepository,
    private val productWebClient: ProductWebClient
) {

    @Transactional
    fun createStore(storeRequest: StoreResponse): StoreEntity {
        if (storeRequest.name.length !in 3..30) {
            throw InvalidDataException("Le nom du magasin doit contenir entre 3 et 30 caractères.")
        }

        // Vérifier si le contact existe, sinon le créer
        val contact = contactRepository.findById(storeRequest.contact.id)
            .orElseGet { contactRepository.save(storeRequest.contact.toEntity()) }

        val store = StoreEntity(
            name = storeRequest.name,
            contact = contact,
            products = emptyList() // La liste de produits est ignorée lors de la création
        )

        return storeRepository.save(store)
    }

    fun getAllStores(): List<StoreEntity> {
        return storeRepository.findAll().sortedBy { it.name }
    }

    fun getStoreById(id: Long): StoreEntity {
        if (id <= 0) throw InvalidDataException("L'ID du magasin est invalide.")
        return storeRepository.findById(id)
            .orElseThrow { StoreNotFoundException("Magasin avec l'ID $id non trouvé.") }
    }

    @Transactional
    fun updateStore(id: Long, storeRequest: StoreResponse): StoreEntity {
        val store = getStoreById(id)

        if (storeRequest.name.length !in 3..30) {
            throw InvalidDataException("Le nom du magasin doit contenir entre 3 et 30 caractères.")
        }

        // Mise à jour du contact si nécessaire
        val updatedContact = contactRepository.findById(storeRequest.contact.id)
            .orElseGet { contactRepository.save(storeRequest.contact.toEntity()) }

        val updatedStore = store.copy(
            name = storeRequest.name,
            contact = updatedContact
        )

        return storeRepository.save(updatedStore)
    }

    @Transactional
    fun deleteStore(id: Long) {
        val store = getStoreById(id)
        storeRepository.delete(store)
    }
}
