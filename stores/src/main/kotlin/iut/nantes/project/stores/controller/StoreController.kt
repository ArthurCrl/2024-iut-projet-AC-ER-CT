package iut.nantes.project.stores.controller

import iut.nantes.project.stores.dto.StoreRequest
import iut.nantes.project.stores.dto.StoreResponse
import iut.nantes.project.stores.service.StoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(
    private val storeService: StoreService
) {

    @PostMapping
    fun createStore(@RequestBody storeRequest: StoreRequest): ResponseEntity<StoreResponse> {
        val createdStore = storeService.createStore(storeRequest)
        return ResponseEntity.status(201).body(createdStore)
    }

    @GetMapping
    fun getAllStores(): ResponseEntity<List<StoreResponse>> {
        val stores = storeService.getAllStores()
        return ResponseEntity.ok(stores)
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Long): ResponseEntity<StoreResponse> {
        val store = storeService.getStoreWithProducts(id)
        return ResponseEntity.ok(store)
    }

    @PutMapping("/{id}")
    fun updateStore(@PathVariable id: Long, @RequestBody storeRequest: StoreRequest): ResponseEntity<StoreResponse> {
        val updatedStore = storeService.updateStore(id, storeRequest)
        return ResponseEntity.ok(updatedStore)
    }

    @DeleteMapping("/{id}")
    fun deleteStore(@PathVariable id: Long): ResponseEntity<Void> {
        storeService.deleteStore(id)
        return ResponseEntity.noContent().build()
    }
}
