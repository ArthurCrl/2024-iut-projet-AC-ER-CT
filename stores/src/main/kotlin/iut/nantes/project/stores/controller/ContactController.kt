package iut.nantes.project.stores.controller

import iut.nantes.project.stores.repository.ContactEntity
import iut.nantes.project.stores.service.ContactService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/contacts")
@Validated
class ContactController(private val contactService: ContactService) {

    @PostMapping
    fun createContact(@RequestBody @Validated contact: ContactEntity): ResponseEntity<ContactEntity> {
        val createdContact = contactService.createContact(contact)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact)
    }

    @GetMapping
    fun getAllContacts(@RequestParam(required = false) city: String?): ResponseEntity<List<ContactEntity>> {
        val contacts = contactService.getAllContacts(city)
        return ResponseEntity.ok(contacts)
    }

    @GetMapping("/{id}")
    fun getContactById(@PathVariable id: Long): ResponseEntity<ContactEntity> {
        val contact = contactService.getContactById(id)
        return ResponseEntity.ok(contact)
    }

    @PutMapping("/{id}")
    fun updateContact(
        @PathVariable id: Long,
        @RequestBody @Validated updatedContact: ContactEntity
    ): ResponseEntity<ContactEntity> {
        val updated = contactService.updateContact(id, updatedContact)
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
