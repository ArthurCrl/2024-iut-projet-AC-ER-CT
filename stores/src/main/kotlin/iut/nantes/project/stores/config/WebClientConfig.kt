package iut.nantes.project.stores.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().baseUrl("http://product-service").build() // Remplacer par l'URL du service Product
    }
}
