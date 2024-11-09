package org.meliapp.backend.integration

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.meliapp.backend.model.User
import org.meliapp.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthIntegrationTest(
    @Autowired val mockMvc: MockMvc
) {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should register a new user and return 201 status code`() {
        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "pepe@email.com", "password": "password" }""")
        )
            .andExpect(status().isCreated).andExpect(jsonPath("$.payload").exists())

        assertTrue { userRepository.findByEmail("pepe@email.com").isPresent }
    }

    @Test
    fun `should not register an existing user and return a 403 status code`() {
        userRepository.save(User().apply {
            roles = listOf()
            email = "pepe@email.com"
            password = passwordEncoder.encode("password")
        })

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "pepe@email.com", "password": "password" }""")
        )
            .andExpect(status().isBadRequest)

    }

    @Test
    fun `should login an existing user and return a 200 status code`() {
        userRepository.save(User().apply {
            roles = listOf()
            email = "pepe@email.com"
            password = passwordEncoder.encode("password")
        })

        mockMvc.perform(
            post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "pepe@email.com", "password": "password" }""")
        )
            .andExpect(status().isOk).andExpect(jsonPath("$.payload").exists())

    }

    @Test
    fun `should not login with bad credentials and return a 401 status code`() {
        // The user doesn't exist on the db
        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"email": "pepe@email.com", "password": "password" }""")
        )
            .andExpect(status().isUnauthorized)


    }

}