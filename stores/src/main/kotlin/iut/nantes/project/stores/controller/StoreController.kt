package iut.nantes.project.stores.controller

import iut.nantes.project.stores.dto.ProductDTO
import iut.nantes.project.stores.dto.StoreDTO
import iut.nantes.project.stores.service.StoreService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(private val storeService: StoreService) {

    @PostMapping
    fun createStore(@RequestBody @Valid storeDTO: StoreDTO): ResponseEntity<StoreDTO> {
        println("Store DTO: $storeDTO")
        val createdStore = storeService.createStore(storeDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore)
    }

    @GetMapping
    fun getAllStores(): ResponseEntity<List<StoreDTO>> {
        return ResponseEntity.ok(storeService.getAllStores())
    }

    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: Long): ResponseEntity<StoreDTO> {
        return ResponseEntity.ok(storeService.getStoreById(id))
    }

    @PutMapping("/{id}")
    fun updateStore(@PathVariable id: Long, @RequestBody @Valid storeDTO: StoreDTO): ResponseEntity<StoreDTO> {
        return ResponseEntity.ok(storeService.updateStore(id, storeDTO))
    }

    @DeleteMapping("/{id}")
    fun deleteStore(@PathVariable id: Long): ResponseEntity<Unit> {
        storeService.deleteStore(id)
        return ResponseEntity.noContent().build()
    }
}
