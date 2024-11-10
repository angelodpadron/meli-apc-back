package org.meliapp.backend.integration.base

import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.BeforeEach
import org.meliapp.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest
@AutoConfigureMockMvc
class BaseIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    lateinit var authToken: String

    @BeforeEach
    fun setupToken() {
        // Clean user db
        userRepository.deleteAll()

        // Register and set the auth token
        val res = mockMvc.perform(
            post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("""{ "email": "test@test.com", "password": "password" }""")
        ).andReturn().response.contentAsString

        authToken = JsonPath.read(res, "$.payload")
    }

    fun makeAuthGetRequest(url: String): ResultActions {

        verifyAuthToken()

        return mockMvc.perform(
            get(url).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer $authToken")
        )
    }

    fun makeAuthPostRequest(url: String, payload: String): ResultActions {
        verifyAuthToken()

        return mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer $authToken")
                .content(payload)
        )

    }

    fun makeAuthPutRequest(url: String, payload: String): ResultActions {
        verifyAuthToken()

        return mockMvc.perform(
            put(url).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer $authToken")
                .content(payload)
        )
    }

    fun makeAuthDeleteRequest(url: String): ResultActions {
        verifyAuthToken()

        return mockMvc.perform(
            delete(url).contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer $authToken")
        )
    }

    private fun verifyAuthToken() {
        if (authToken.isEmpty()) throw RuntimeException("Cannot make request. Token is empty.")
    }

}