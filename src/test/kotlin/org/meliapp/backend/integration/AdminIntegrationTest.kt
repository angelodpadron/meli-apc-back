package org.meliapp.backend.integration

import com.jayway.jsonpath.JsonPath
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.meliapp.backend.integration.base.BaseIntegrationTest
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@Transactional
class AdminIntegrationTest : BaseIntegrationTest() {

    // These values must be configured inside the application properties file
    @Value("\${app.apc.admin.email}")
    lateinit var adminEmail: String
    @Value("\${app.apc.admin.password}")
    lateinit var adminPassword: String

    @BeforeEach
    override fun setupToken() {
        val res = mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email":  "$adminEmail", "password": "$adminPassword" }"""))
            .andReturn()
            .response
            .contentAsString

        authToken = JsonPath.read(res, "$.payload")

    }

    @Test
    fun `should get all registered user and a 200 status code`() {
        makeAuthGetRequest("/api/admin/registered-users")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.payload").exists())
    }

    @Test
    fun `should get top five most bookmarked products and a 200 status code`() {
        makeAuthGetRequest("/api/admin/top-five-bookmarked")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.payload").exists())
    }

    @Test
    fun `should get top five most purchased products and a 200 status code`() {
        makeAuthGetRequest("/api/admin/top-five-sold")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.payload").exists())
    }

    @Test
    fun `should get top five buyers and a 200 status code`() {
        makeAuthGetRequest("/api/admin/top-five-sold")
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.payload").exists())
    }

}