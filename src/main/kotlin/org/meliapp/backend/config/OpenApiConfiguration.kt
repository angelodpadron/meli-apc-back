package org.meliapp.backend.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class OpenApiConfiguration {

    @Value("\${app.base-url}")
    lateinit var appUrl: String

    @Bean
    fun defineOpenApi(): OpenAPI {
        val server = Server()
        server.url(appUrl)

        val info = Info()
                .title("APC Meli")
                .version("0.1")
                .description("API de Asesor Personal de Compras")

        return OpenAPI()
                .info(info)
                .servers(listOf(server))
    }

}