package iut.nantes.project.products.repository

import jakarta.persistence.*
import java.util.*

@Entity
data class FamilyJpa(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(unique = true, nullable = false, length = 30)
    val name: String,

    @Column(nullable = false, length = 100)
    val description: String
)