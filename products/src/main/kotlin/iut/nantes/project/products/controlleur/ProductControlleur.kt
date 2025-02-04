package iut.nantes.project.products.controlleur

import iut.nantes.project.products.controlleur.dto.ProductRequest
import iut.nantes.project.products.controlleur.dto.ProductResponse
import iut.nantes.project.products.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/products")
class ProductControlleur(private val productService: ProductService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody request: ProductRequest): ProductResponse {
        return try {
            productService.createProduct(request)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping
    fun getProducts(
        @RequestParam(required = false) familyname: String?,
        @RequestParam(required = false) minprice: Double?,
        @RequestParam(required = false) maxprice: Double?
    ): List<ProductResponse> {
        return try {
            productService.getProducts(familyname, minprice, maxprice)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: String): ProductResponse {
        return try {
            productService.getProductById(id)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: String,
        @RequestBody request: ProductRequest
    ): ProductResponse {
        return try {
            productService.updateProduct(id, request)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        }
    }
}