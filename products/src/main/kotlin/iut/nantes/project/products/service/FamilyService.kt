package iut.nantes.project.products.service

import iut.nantes.project.products.controlleur.dto.FamilyRequest
import iut.nantes.project.products.controlleur.dto.FamilyResponse
import iut.nantes.project.products.repository.FamilyJpa
import iut.nantes.project.products.repository.FamilyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.NoSuchElementException

@Service
@Transactional
class FamilyService(private val familyRepository: FamilyRepository) {

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
        val uuid = UUID.fromString(id)

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

    private fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

}