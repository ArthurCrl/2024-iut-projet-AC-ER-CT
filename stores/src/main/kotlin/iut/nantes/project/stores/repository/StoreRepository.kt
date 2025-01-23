package iut.nantes.project.stores.repository

import iut.nantes.project.stores.repository.StoreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<StoreEntity, Long> {
    fun findByName(name: String): List<StoreEntity>
}
