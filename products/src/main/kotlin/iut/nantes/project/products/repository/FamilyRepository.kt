package iut.nantes.project.products.repository

import org.springframework.data.jpa.repository.JpaRepository

interface FamilyRepository : JpaRepository<FamilyJpa, String> {
    fun findByName(name: String): FamilyJpa?
}