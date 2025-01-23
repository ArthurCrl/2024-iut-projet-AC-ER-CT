package iut.nantes.project.stores.service

import iut.nantes.project.stores.repository.ProductEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class ProductServiceClient(private val webClient: WebClient) {

    // Récupérer les produits associés à un magasin via l'ID du magasin
    suspend fun getProductsForStore(storeId: Long): List<ProductEntity> {
        return webClient.get()
            .uri("http://product-service/api/v1/products?storeId={storeId}", storeId) // Ajuster l'URL du service
            .retrieve()
            .awaitBody() // Attend la réponse et retourne la liste des produits
    }
}
