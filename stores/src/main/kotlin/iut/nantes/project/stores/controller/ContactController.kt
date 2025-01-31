package iut.nantes.project.stores.controller

import iut.nantes.project.stores.dto.ContactDTO
import iut.nantes.project.stores.entity.ContactEntity
import iut.nantes.project.stores.service.ContactService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/contacts")
class ContactController(private val contactService: ContactService) {

    @PostMapping
    fun createContact(@RequestBody @Valid contactDTO: ContactDTO): ResponseEntity<ContactDTO> {
        println("Store DTO:")
        val createdContact = contactService.createContact(contactDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact)
    }

    @GetMapping
    fun getAllContacts(@RequestParam(required = false) city: String?): ResponseEntity<List<ContactDTO>> {
        val contacts = contactService.getAllContacts(city)
        return ResponseEntity.ok(contacts)
    }

    @GetMapping("/{id}")
    fun getContactById(@PathVariable id: Long): ResponseEntity<ContactDTO> {
        val contact = contactService.getContactById(id)
        return ResponseEntity.ok(contact)
    }

    @PutMapping("/{id}")
    fun updateContact(
        @PathVariable id: Long,
        @RequestBody @Valid updatedContactDTO: ContactDTO
    ): ResponseEntity<ContactDTO> {
        val updated = contactService.updateContact(id, updatedContactDTO)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteContact(
        @PathVariable id: Long,
        @RequestParam(required = false, defaultValue = "false") hasLinkedStore: Boolean
    ): ResponseEntity<Unit> {
        contactService.deleteContact(id, hasLinkedStore)
        return ResponseEntity.noContent().build()
    }
}

