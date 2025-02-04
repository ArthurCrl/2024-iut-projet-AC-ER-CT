package iut.nantes.project.products.controlleur

import iut.nantes.project.products.controlleur.dto.FamilyRequest
import iut.nantes.project.products.controlleur.dto.FamilyResponse
import iut.nantes.project.products.service.FamilyService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/families")
class FamilyControlleur(private val familyService: FamilyService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFamily(@RequestBody request: FamilyRequest): FamilyResponse {
        return try {
            familyService.createFamily(request)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
        }
    }

    @GetMapping
    fun getAllFamilies(): List<FamilyResponse> {
        return familyService.getAllFamilies()
    }

    @GetMapping("/{id}")
    fun getFamilyById(@PathVariable id: String): FamilyResponse {
        return try {
            familyService.getFamilyById(id)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format")
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found")
        }
    }

    @PutMapping("/{id}")
    fun updateFamily(
        @PathVariable id: String,
        @RequestBody request: FamilyRequest
    ): FamilyResponse {
        return try {
            familyService.updateFamily(id, request)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: IllegalStateException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFamily(@PathVariable id: String) {
        try {
            familyService.deleteFamilyById(id)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: NoSuchElementException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: IllegalStateException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
        }
    }
}