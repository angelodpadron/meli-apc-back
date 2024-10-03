package org.meliapp.backend.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import org.meliapp.backend.dto.ApiResponse
import org.meliapp.backend.dto.meli.MeliSearchResponse
import org.meliapp.backend.service.MeliSearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

@RestController
@RequestMapping("/api/products")
class MeliSearchController(private val meliSearchService: MeliSearchService) {

    @GetMapping("/search")
    @Operation(
        summary = "Find items by keyword",
        description = "Searches for items using a keyword in the MercadoLibre API.",
        responses = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "Successful search results",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MeliSearchResponse::class)
                    )
                ]
            ),
            SwaggerApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @Parameters(
        Parameter(
            name = "keyword",
            description = "The keyword used to search for items.",
            required = true,
            example = "don satur"
        ),
        Parameter(
            name = "params",
            description = "Filters to be applied on the search. This is a dynamic set of key-value pairs.",
            required = false,
            schema = Schema(
                type = "object",
            ),
            examples = [
                ExampleObject(
                    name = "filters",
                    value = "{\"discount\": \"10-100\", \"shipping\": \"mercadoenvios\"}"
                )
            ],
        )
    )
    fun findByKeyword(@RequestParam keyword: String, @RequestParam(required = false) filters: Map<String, String>): ResponseEntity<ApiResponse<MeliSearchResponse>> {
        return ResponseEntity.ok(ApiResponse(meliSearchService.findByKeyword(keyword, filters)))
    }

}