package iut.nantes.project.products.config

import iut.nantes.project.products.repository.FamilyRepository
import iut.nantes.project.products.repository.ProductRepository
import iut.nantes.project.products.service.FamilyService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FamilyConfig {

    @Bean
    fun familyService(
        productRepository: ProductRepository,
        familyRepository: FamilyRepository
    ): FamilyService {
        return FamilyService(familyRepository, productRepository, )
    }
}
