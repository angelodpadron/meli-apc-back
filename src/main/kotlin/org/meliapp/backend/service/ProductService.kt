package org.meliapp.backend.service

import org.meliapp.backend.model.Product
import org.meliapp.backend.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val meliSearchService: MeliSearchService
) {

    fun findByMeliId(id: String): Product {
        return productRepository.findByMeliId(id)
            .orElseGet {
                meliSearchService.findById(id).let {
                    productRepository.save(Product().apply {
                        title = it.title
                        price = it.price
                        thumbnail = it.thumbnail
                        meliId = id
                    })
                }
            }
    }

    fun findByMeliIdOld(meliId: String): Optional<Product> {
        return productRepository.findByMeliId(meliId)
    }

}