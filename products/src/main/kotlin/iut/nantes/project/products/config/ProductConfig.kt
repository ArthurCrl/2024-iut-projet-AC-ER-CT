package iut.nantes.project.products.config

import iut.nantes.project.products.repository.FamilyRepository
import iut.nantes.project.products.repository.ProductRepository
import iut.nantes.project.products.service.ProductService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ProductConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }

    @Bean
    fun productService(
        productRepository: ProductRepository,
        familyRepository: FamilyRepository,
        webClientBuilder: WebClient.Builder
    ): ProductService {
        val webClient = webClientBuilder.baseUrl("http://localhost:8082") // Remplacez par l'URL réelle
            .build()
        return ProductService(productRepository, familyRepository, webClient)
    }
}