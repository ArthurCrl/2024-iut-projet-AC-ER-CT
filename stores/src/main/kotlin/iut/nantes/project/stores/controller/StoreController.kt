package iut.nantes.project.stores.controller

import iut.nantes.project.stores.dto.ProductDTO
import iut.nantes.project.stores.dto.StoreDTO
import iut.nantes.project.stores.service.StoreService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(private val storeService: StoreService) {

    @PostMapping
    fun createStore(@RequestBody @Valid storeDTO: StoreDTO): ResponseEntity<StoreDTO> {
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

    @PostMapping("/{storeId}/products/{productId}/add")
    fun addProductToStock(
        @PathVariable storeId: Long,
        @PathVariable productId: UUID,
        @RequestParam(required = false, defaultValue = "1") quantity: Int
    ): ResponseEntity<ProductDTO> {
        val updatedProduct = storeService.addProductToStock(storeId, productId, quantity)
        return ResponseEntity.ok(updatedProduct)
    }

    @PostMapping("/{storeId}/products/{productId}/remove")
    fun removeProductFromStock(
        @PathVariable storeId: Long,
        @PathVariable productId: UUID,
        @RequestParam(required = false, defaultValue = "1") quantity: Int
    ): ResponseEntity<ProductDTO> {
        val updatedProduct = storeService.removeProductFromStock(storeId, productId, quantity)
        return ResponseEntity.ok(updatedProduct)
    }

    // Suppression de plusieurs produits du stock
    @DeleteMapping("/{storeId}/products")
    fun deleteProductsFromStock(
        @PathVariable storeId: Long,
        @RequestBody productIds: List<UUID>
    ): ResponseEntity<Unit> {
        storeService.deleteProductsFromStock(storeId, productIds)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/products/{productId}/stock")
    fun checkProductStock(@PathVariable productId: UUID): ResponseEntity<Boolean> {
        val isInStock = storeService.isProductInStock(productId)
        return ResponseEntity.ok(isInStock)
    }
}
