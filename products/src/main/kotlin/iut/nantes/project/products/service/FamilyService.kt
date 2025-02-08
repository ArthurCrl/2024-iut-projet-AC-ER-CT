package iut.nantes.project.products.service

import iut.nantes.project.products.dto.FamilyRequest
import iut.nantes.project.products.dto.FamilyResponse
import iut.nantes.project.products.repository.FamilyJpa
import iut.nantes.project.products.repository.FamilyRepository
import iut.nantes.project.products.repository.ProductRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.util.*
import kotlin.NoSuchElementException

@Transactional
class FamilyService(
    private val familyRepository: FamilyRepository,
    private val productRepository: ProductRepository
) {

    fun createFamily(request: FamilyRequest): FamilyResponse {
        // Vérifie si le nom est déjà utilisé
        if (familyRepository.findByName(request.name) != null) {
            throw IllegalArgumentException("Family name already exists")
        }

        // Valide les contraintes
        if (request.name.length !in 3..30 || request.description.length !in 5..100) {
            throw IllegalArgumentException("Invalid name or description length")
        }

        // Crée et sauvegarde la famille
        val family = FamilyJpa(name = request.name, description = request.description)
        familyRepository.save(family)

        return FamilyResponse(
            id = family.id,
            name = family.name,
            description = family.description
        )
    }

    fun getAllFamilies(): List<FamilyResponse> {
        return familyRepository.findAll().map { family ->
            FamilyResponse(
                id = family.id,
                name = family.name,
                description = family.description
            )
        }
    }

    private fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getFamilyById(id: String): FamilyResponse {
        // Vérification du format de l'UUID
        if (!isValidUUID(id)) {
            throw IllegalArgumentException("Invalid UUID format")
        }

        val family = familyRepository.findById(id)
            .orElseThrow { NoSuchElementException("No family found with the given ID") }

        return FamilyResponse(
            id = family.id,
            name = family.name,
            description = family.description
        )
    }

    fun updateFamily(id: String, request: FamilyRequest): FamilyResponse {
        // Validation de l'ID
        if (!isValidUUID(id)) {
            throw IllegalArgumentException("Invalid UUID format")
        }

        // Vérification de l'existence de la famille
        val existingFamily = familyRepository.findById(id)
            .orElseThrow { NoSuchElementException("No family found with the given ID") }


        // Validation des champs
        if (request.name.length !in 3..30 || request.description.length !in 5..100) {
            throw IllegalArgumentException("Invalid name or description length")
        }

        // Vérification du conflit de nom avec une autre famille
        val otherFamilyName = familyRepository.findByName(request.name)
        if (otherFamilyName != null) {
            if (otherFamilyName.id != existingFamily.id) {
                throw IllegalStateException("Family name already exists")
            }
        }

        // Mise à jour des champs
        val updatedFamily = existingFamily.copy(
            name = request.name,
            description = request.description
        )

        familyRepository.save(updatedFamily)

        return FamilyResponse(
            id = updatedFamily.id,
            name = updatedFamily.name,
            description = updatedFamily.description
        )
    }

    fun deleteFamilyById(id: String) {
        if (!isValidUUID(id)) {
            throw IllegalArgumentException("Invalid UUID format")
        }

        val family = familyRepository.findById(id)
            .orElseThrow { NoSuchElementException("No family found with the given ID") }

        // Vérification de la présence de produits liés à cette famille
        val hasLinkedProducts = productRepository.existsByFamilyId(id)
        if (hasLinkedProducts) {
            throw IllegalStateException("Cannot delete family, products are still linked to it")
        }

        familyRepository.delete(family)
    }



}