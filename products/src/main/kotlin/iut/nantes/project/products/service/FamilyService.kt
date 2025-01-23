package iut.nantes.project.products.service

import iut.nantes.project.products.controlleur.dto.FamilyRequest
import iut.nantes.project.products.controlleur.dto.FamilyResponse
import iut.nantes.project.products.repository.FamilyJpa
import iut.nantes.project.products.repository.FamilyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
}