package iut.nantes.project.stores.service

import iut.nantes.project.stores.dto.ContactDTO
import iut.nantes.project.stores.exception.ContactExistInMagasinExecption
import iut.nantes.project.stores.exception.ContactNotFoundException
import iut.nantes.project.stores.exception.InvalidContactDataException
import iut.nantes.project.stores.repository.ContactRepository
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
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
            throw ContactNotFoundException("Contact non trouvé pour l'ID $id")
        }

        return contact.toDto()
    }

    fun updateContact(id: Long, updatedContactDTO: ContactDTO): ContactDTO {
        val existingContact = contactRepository.findById(id).orElseThrow {
            throw InvalidContactDataException("Les données sont invalides")
        }

        if (existingContact.email != updatedContactDTO.email && existingContact.phone != updatedContactDTO.phone) {
            throw InvalidContactDataException("Vous ne pouvez pas changer à la fois l'email et le téléphone.")
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
            throw ContactExistInMagasinExecption("Le contact est lié à un magasin, suppression impossible.")
        }

        if (!contactRepository.existsById(id)) {
            throw InvalidContactDataException("L'id est invalide")
        }
        contactRepository.deleteById(id)
    }
}
