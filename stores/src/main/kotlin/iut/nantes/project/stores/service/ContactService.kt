package iut.nantes.project.stores.service

import iut.nantes.project.stores.dto.ContactDTO
import iut.nantes.project.stores.entity.ContactEntity
import iut.nantes.project.stores.repository.ContactRepository
import org.springframework.stereotype.Service

@Service
class ContactService(private val contactRepository: ContactRepository) {

    fun createContact(contactDTO: ContactDTO): ContactDTO {
        val contactEntity = contactDTO.toEntity()
        val savedEntity = contactRepository.save(contactEntity)
        return savedEntity.toDto()
    }

    fun getAllContacts(city: String?): List<ContactDTO> {
        val contacts = if (city.isNullOrEmpty()) {
            contactRepository.findAll()
        } else {
            contactRepository.findByAddressCity(city)
        }
        return contacts.map { it.toDto() }
    }

    fun getContactById(id: Long): ContactDTO {
        val contact = contactRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("Contact non trouvé pour l'id $id")
        }
        return contact.toDto()
    }

    fun updateContact(id: Long, updatedContactDTO: ContactDTO): ContactDTO {
        val existingContact = contactRepository.findById(id).orElseThrow {
            throw IllegalArgumentException("Contact non trouvé pour l'id $id")
        }

        // Vérifier qu'on ne change pas à la fois l'email et le téléphone
        if (existingContact.email != updatedContactDTO.email && existingContact.phone != updatedContactDTO.phone) {
            throw IllegalArgumentException("Vous ne pouvez pas changer à la fois l'email et le téléphone.")
        }

        val contactToSave = existingContact.copy(
            email = updatedContactDTO.email,
            phone = updatedContactDTO.phone,
            address = updatedContactDTO.address.toEntity()
        )
        val savedContact = contactRepository.save(contactToSave)
        return savedContact.toDto()
    }

    fun deleteContact(id: Long, hasLinkedStore: Boolean) {
        if (hasLinkedStore) {
            throw IllegalStateException("Le contact est lié à un magasin, suppression impossible.")
        }

        if (!contactRepository.existsById(id)) {
            throw IllegalArgumentException("Contact non trouvé pour l'id $id")
        }
        contactRepository.deleteById(id)
    }
}
