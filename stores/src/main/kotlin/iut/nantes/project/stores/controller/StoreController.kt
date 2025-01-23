package iut.nantes.project.stores.controller

import iut.nantes.project.stores.repository.StoreEntity
import iut.nantes.project.stores.service.StoreService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(private val storeService: StoreService) {

    // Créer un magasin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createStore(@RequestBody store: StoreEntity): StoreEntity {
        return storeService.createStore(store)
    }

    // Récupérer un magasin par son ID
    @GetMapping("/{id}")
    suspend fun getStoreById(@PathVariable id: Long): StoreEntity {
        return storeService.getStoreById(id)
    }

    // Récupérer tous les magasins triés par nom
    @GetMapping
    fun getAllStores(): List<StoreEntity> {
        return storeService.getAllStores()
    }

    // Mettre à jour un magasin (sans changer les produits)
    @PutMapping("/{id}")
    fun updateStore(@PathVariable id: Long, @RequestBody store: StoreEntity): StoreEntity {
        return storeService.updateStore(id, store)
    }

    // Supprimer un magasin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStore(@PathVariable id: Long) {
        storeService.deleteStore(id)
    }
}
