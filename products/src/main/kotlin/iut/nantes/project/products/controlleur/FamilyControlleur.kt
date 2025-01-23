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
}