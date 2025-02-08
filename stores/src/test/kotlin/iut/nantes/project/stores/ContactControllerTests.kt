package iut.nantes.project.stores

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath



@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTests @Autowired constructor(
    val mockMvc: MockMvc
) {

    @Test
    fun `POST create contact - valid input`() {
        val contactJson = """
            {
              "email": "test@email.com",
              "phone": "0123456789",
              "address": {
                "street": "10 Rue truc",
                "city": "Nantes",
                "postalCode": "44300"
              }
            }
        """
        mockMvc.perform(
            post("/api/v1/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contactJson)
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.email").value("test@email.com"))
            .andExpect(jsonPath("$.phone").value("0123456789"))
            .andExpect(jsonPath("$.address.street").value("10 Rue truc"))
    }

    @Test
    fun `POST create contact - invalid input`() {
        val invalidContactJson = """
            {
              "email": "invalid-email",
              "phone": "123",
              "address": {
                "street": "Rue",
                "city": "",
                "postalCode": "1234"
              }
            }
        """
        mockMvc.perform(
            post("/api/v1/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidContactJson)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `GET contacts by city - valid`() {
        mockMvc.perform(
            get("/api/v1/contacts?city=Nantes")
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `GET contact by ID - not found`() {
        mockMvc.perform(
            get("/api/v1/contacts/999")
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `PUT update contact - invalid`() {
        val updateContactJson = """
            {
              "email": "new@email.com",
              "phone": "0123456789",
              "address": {
                "street": "Updated Street",
                "city": "Updated City",
                "postalCode": "44300"
              }
            }
        """
        mockMvc.perform(
            put("/api/v1/contacts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateContactJson)
        ).andExpect(status().isBadRequest) // Violates "no simultaneous change to email and phone"
    }
}
